package com.jobdam.admin.service;

import com.jobdam.admin.dto.ReportRequestDto;
import com.jobdam.admin.dto.ReportResponseDto;
import com.jobdam.admin.entity.Report;
import com.jobdam.admin.repository.ReportRepository;
import com.jobdam.community.entity.CommunityPost;
import com.jobdam.community.repository.CommunityPostRepository;
import com.jobdam.sns.entity.SnsPost;
import com.jobdam.sns.repository.SnsPostRepository;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CommunityPostRepository communityPostRepository;
    private final SnsPostRepository snsPostRepository;

    // 신고 등록
    public void createReport(ReportRequestDto dto, Integer userId) {
        boolean isDuplicate = reportRepository.existsByUserIdAndTargetId(userId, dto.getTargetId());
        if (isDuplicate) throw new IllegalArgumentException("이미 해당 대상에 대해 신고하셨습니다.");

        Report report = new Report();
        report.setReportTypeCodeId(dto.getReportTypeCodeId());
        report.setTargetId(dto.getTargetId());
        report.setUserId(userId);
        report.setReason(dto.getReason());
        // createdAt은 @PrePersist로 자동세팅

        reportRepository.save(report);
    }

    // 목록(페이징, 동적검색 옵션 가능)
    public Page<ReportResponseDto> getReports(Pageable pageable) {
        Page<Report> page = reportRepository.findAll(pageable);
        return page.map(this::toDto);
    }

    // 단건 상세
    public ReportResponseDto getReportById(Integer reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("신고 내역이 없습니다."));
        return toDto(report);
    }

    // 삭제(=처리완료)
    public void deleteReport(Integer reportId) {
        if (!reportRepository.existsById(reportId))
            throw new EntityNotFoundException("해당 신고 내역이 없습니다.");
        reportRepository.deleteById(reportId);
    }

    // === 신고대상 회원 활동정지 및 신고 처리완료 ===
    @Transactional
    public void processReportAndDeactivate(Integer reportId) {
        System.out.println("==== processReportAndDeactivate 진입, reportId=" + reportId);

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("해당 신고가 없습니다."));
        System.out.println("==== Report 로드 완료, report=" + report);

        report.setStatus(2); // 2: 정지
        report.setProcessedAt(LocalDateTime.now());
        reportRepository.save(report);
        System.out.println("==== 신고 상태(status=2) 및 처리일시 저장 완료");

        // 피신고자 찾기 (reportTypeCodeId 기준 분기)
        Integer reportedUserId = getReportedUserId(report);
        System.out.println("==== 피신고자 userId=" + reportedUserId);

        User user = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new EntityNotFoundException("피신고 회원 없음"));
        System.out.println("==== User 로드 완료, isActive(before)=" + user.getIsActive());

        user.setIsActive(false); // is_active=0
        userRepository.save(user);
        System.out.println("==== User isActive 업데이트 완료, isActive(after)=" + user.getIsActive());
    }

    // === 신고 허용(처리완료) ===
    @Transactional
    public void approveReport(Integer reportId) {
        System.out.println("==== approveReport 진입, reportId=" + reportId);
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("해당 신고가 없습니다."));

        report.setStatus(1); // 1: 허용(그냥 처리완료)
        report.setProcessedAt(LocalDateTime.now());
        reportRepository.save(report);
        System.out.println("==== 신고 허용(status=1) 및 처리일시 저장 완료");
    }

    // ====== 핵심: 신고 대상에 따라 피신고자 userId 찾기 ======
    private Integer getReportedUserId(Report report) {
        Integer typeCodeId = report.getReportTypeCodeId();
        Long targetId = report.getTargetId();

        if (typeCodeId == 1) { // 1 = 커뮤니티 게시글
            return communityPostRepository.findById(targetId.intValue())
                    .map(CommunityPost::getUserId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 커뮤니티 게시글 없음"));
        } else if (typeCodeId == 2) { // 2 = SNS 게시글
            return snsPostRepository.findById(targetId.intValue())
                    .map(SnsPost::getUserId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 SNS 게시글 없음"));
        }
        throw new EntityNotFoundException("지원하지 않는 신고 대상 타입입니다.");
    }

    // ====== Entity → DTO 변환 ======
    private ReportResponseDto toDto(Report report) {
        ReportResponseDto dto = new ReportResponseDto();
        dto.setReportId(report.getReportId());
        dto.setReportTypeCodeId(report.getReportTypeCodeId());
        dto.setTargetId(report.getTargetId());
        dto.setUserId(report.getUserId());
        dto.setReason(report.getReason());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setStatus(report.getStatus());
        dto.setProcessedAt(report.getProcessedAt());
        dto.setReporterEmail(report.getUser().getEmail());
        dto.setReporterNickname(report.getUser().getNickname());
        dto.setReportTypeName(report.getReportTypeCode().getName());

        // 피신고자 닉네임
        Integer typeCodeId = report.getReportTypeCodeId();
        Long targetId = report.getTargetId();
        String reportedNickname = "(알수없음)";
        if (typeCodeId == 1) { // 커뮤니티 게시글
            reportedNickname = communityPostRepository.findById(targetId.intValue())
                    .map(post -> {
                        User user = userRepository.findById(post.getUserId()).orElse(null);
                        return user != null ? user.getNickname() : "(알수없음)";
                    }).orElse("(알수없음)");
        } else if (typeCodeId == 2) { // SNS 게시글
            reportedNickname = snsPostRepository.findById(targetId.intValue())
                    .map(post -> {
                        User user = userRepository.findById(post.getUserId()).orElse(null);
                        return user != null ? user.getNickname() : "(알수없음)";
                    }).orElse("(알수없음)");
        }
        dto.setReportedNickname(reportedNickname);

        return dto;
    }
}
