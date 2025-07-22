package com.jobdam.admin.service;

import com.jobdam.admin.dto.ReportRequestDto;
import com.jobdam.admin.dto.ReportResponseDto;
import com.jobdam.admin.entity.Report;
import com.jobdam.admin.repository.ReportRepository;
import com.jobdam.community.entity.CommunityPost;
import com.jobdam.community.repository.CommunityPostRepository;
import com.jobdam.community.entity.CommunityComment;
import com.jobdam.community.repository.CommunityCommentRepository;
import com.jobdam.sns.entity.SnsPost;
import com.jobdam.sns.repository.SnsPostRepository;
import com.jobdam.sns.entity.SnsComment;
import com.jobdam.sns.repository.SnsCommentRepository;
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
    private final CommunityCommentRepository communityCommentRepository;
    private final SnsCommentRepository snsCommentRepository;

    // 신고 등록
    public void createReport(ReportRequestDto dto, Integer userId) {
        boolean isDuplicate = reportRepository.existsByUserIdAndTargetId(userId, dto.getTargetId());
        if (isDuplicate)
            throw new IllegalArgumentException("이미 해당 대상에 대해 신고하셨습니다.");

        Report report = new Report();
        report.setReportTypeCodeId(dto.getReportTypeCodeId());
        report.setTargetId(dto.getTargetId());
        report.setUserId(userId);
        report.setReason(dto.getReason());
        reportRepository.save(report);
    }

    // 목록(페이징, status 필터 가능)
    public Page<ReportResponseDto> getReports(Integer status, Pageable pageable) {
        Page<Report> page = (status == null)
                ? reportRepository.findAll(pageable)
                : reportRepository.findByStatus(status, pageable);
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

        // 피신고자 찾기 (자동 구분)
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

    // ====== 자동: 신고 대상에 따라 피신고자 userId 찾기 ======
    private Integer getReportedUserId(Report report) {
        Integer typeCodeId = report.getReportTypeCodeId();
        Long targetId = report.getTargetId();

        if (typeCodeId == 1) { // 게시글(커뮤니티/SNS 둘 다)
            // 커뮤니티 > SNS 순으로 PK 우선 조회
            return communityPostRepository.findById(targetId.intValue())
                    .map(CommunityPost::getUserId)
                    .orElseGet(() -> snsPostRepository.findById(targetId.intValue())
                            .map(SnsPost::getUserId)
                            .orElseThrow(() -> new EntityNotFoundException("해당 게시글 없음")));
        } else if (typeCodeId == 2) { // 댓글(커뮤니티/SNS 둘 다)
            return communityCommentRepository.findById(targetId.intValue())
                    .map(CommunityComment::getUserId)
                    .orElseGet(() -> snsCommentRepository.findById(targetId.intValue())
                            .map(SnsComment::getUserId)
                            .orElseThrow(() -> new EntityNotFoundException("해당 댓글 없음")));
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

        // 피신고자 닉네임(커뮤니티→SNS 순)
        Integer typeCodeId = report.getReportTypeCodeId();
        Long targetId = report.getTargetId();

        String reportedNickname = null;
        try {
            if (typeCodeId == 1) { // 게시글
                reportedNickname = communityPostRepository.findById(targetId.intValue())
                        .map(post -> userRepository.findById(post.getUserId())
                                .map(User::getNickname).orElse(null))
                        .orElseGet(() -> snsPostRepository.findById(targetId.intValue())
                                .map(post -> userRepository.findById(post.getUserId())
                                        .map(User::getNickname).orElse(null))
                                .orElse(null));
            } else if (typeCodeId == 2) { // 댓글
                reportedNickname = communityCommentRepository.findById(targetId.intValue())
                        .map(comment -> userRepository.findById(comment.getUserId())
                                .map(User::getNickname).orElse(null))
                        .orElseGet(() -> snsCommentRepository.findById(targetId.intValue())
                                .map(comment -> userRepository.findById(comment.getUserId())
                                        .map(User::getNickname).orElse(null))
                                .orElse(null));
            }
        } catch (Exception e) {
            reportedNickname = null;
        }
        dto.setReportedNickname(reportedNickname != null ? reportedNickname : "(알수없음)");
        return dto;
    }
}
