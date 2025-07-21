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

    // 목록(페이징, status 필터 가능)
    public Page<ReportResponseDto> getReports(Integer status, Pageable pageable) {
        Page<Report> page;
        if (status == null) {
            page = reportRepository.findAll(pageable);
        } else {
            page = reportRepository.findByStatus(status, pageable);
        }
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
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("해당 신고가 없습니다."));
        report.setStatus(2); // 2: 정지
        report.setProcessedAt(LocalDateTime.now());
        reportRepository.save(report);

        // 피신고자 찾기 (reportTypeCodeId 기준 분기)
        Integer reportedUserId = getReportedUserId(report);
        User user = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new EntityNotFoundException("피신고 회원 없음"));

        user.setIsActive(false); // is_active=0
        userRepository.save(user);
    }

    // === 신고 허용(처리완료) ===
    @Transactional
    public void approveReport(Integer reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("해당 신고가 없습니다."));

        report.setStatus(1); // 1: 허용(그냥 처리완료)
        report.setProcessedAt(LocalDateTime.now());
        reportRepository.save(report);
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

    // === Entity → DTO 변환 ===
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

        try {
            if (typeCodeId == 1 && targetId != null) { // 커뮤니티 게시글
                communityPostRepository.findById(targetId.intValue())
                        .ifPresent(post -> {
                            userRepository.findById(post.getUserId())
                                    .ifPresent(user -> dto.setReportedNickname(user.getNickname()));
                        });
            } else if (typeCodeId == 2 && targetId != null) { // SNS 게시글
                snsPostRepository.findById(targetId.intValue())
                        .ifPresent(post -> {
                            userRepository.findById(post.getUserId())
                                    .ifPresent(user -> dto.setReportedNickname(user.getNickname()));
                        });
            }
        } catch (Exception e) {
            // 혹시라도 변환/조회 중 예외 발생 시 그냥 알수없음
            dto.setReportedNickname("(알수없음)");
        }
        // 혹시 위에서 닉네임 못 채웠으면 기본값 유지
        if (dto.getReportedNickname() == null) {
            dto.setReportedNickname("(알수없음)");
        }

        return dto;
    }


}
