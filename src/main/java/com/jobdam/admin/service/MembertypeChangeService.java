package com.jobdam.admin.service;

import com.jobdam.admin.dto.MembertypeChangeDetailResponseDto;
import com.jobdam.admin.dto.MembertypeChangeRequestDto;
import com.jobdam.admin.dto.MembertypeChangeResponseDto;
import com.jobdam.admin.entity.MembertypeChange;
import com.jobdam.admin.repository.MembertypeChangeRepository;
import com.jobdam.code.entity.AdminStatusCode;
import com.jobdam.code.entity.MemberTypeCode;
import com.jobdam.code.repository.AdminStatusCodeRepository;
import com.jobdam.code.repository.MemberTypeCodeRepository;
import com.jobdam.common.service.FileService;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembertypeChangeService {

    private final MembertypeChangeRepository membertypeChangeRepository;
    private final MemberTypeCodeRepository memberTypeCodeRepository;
    private final UserRepository userRepository;
    private final AdminStatusCodeRepository adminStatusCodeRepository;
    private final FileService fileService;

    public void createRequest(Integer userId, MembertypeChangeRequestDto dto) {

        boolean hasPending = membertypeChangeRepository.existsByUserIdAndRequestAdminStatusCode_Code(userId, "PENDING");
        if (hasPending) {
            throw new IllegalStateException("이미 처리 대기 중인 신청이 존재합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        MemberTypeCode currentType = user.getMemberTypeCode();
        if (currentType == null) throw new RuntimeException("회원 타입 정보가 없습니다.");

        MemberTypeCode requestedType = memberTypeCodeRepository.findByCode(dto.getRequestedMemberTypeCode())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 요청 회원 타입 코드"));

        // ==== 첨부파일 저장 방식만 변경 ====
        String fileUrl = null;
        if (dto.getAttachment() != null && !dto.getAttachment().isEmpty()) {
            String fileId = fileService.saveFile(dto.getAttachment());
            fileUrl = "/api/files/" + fileId;
        }

        MembertypeChange entity = MembertypeChange.builder()
                .userId(userId)
                .currentMemberTypeCodeId(currentType.getMemberTypeCodeId())
                .requestedMemberTypeCodeId(requestedType.getMemberTypeCodeId())
                .title(dto.getTitle())
                .reason(dto.getReason())
                .content(dto.getContent())
                .referenceLink(dto.getReferenceLink())
                .attachmentUrl(fileUrl) // 여기에 저장
                .requestedAt(LocalDateTime.now())
                .processedAt(null)
                .requestAdminStatusCodeId(1)
                .build();

        membertypeChangeRepository.save(entity);
    }


    @Transactional
    public void processRequest(Integer requestId, String statusCode) {

        MembertypeChange request = membertypeChangeRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청 내역을 찾을 수 없습니다."));

        AdminStatusCode status = adminStatusCodeRepository.findByCode(statusCode)
                .orElseThrow(() -> new RuntimeException("잘못된 처리 상태 코드입니다."));

        request.setRequestAdminStatusCodeId(status.getAdminStatusCodeId());
        request.setProcessedAt(LocalDateTime.now());


        if (statusCode.equals("APPROVED")) {
            User user = request.getUser();
            user.setMemberTypeCodeId(request.getRequestedMemberTypeCodeId());
        }
    }

    public List<MembertypeChangeResponseDto> getAll() {
        return membertypeChangeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<MembertypeChangeResponseDto> getPendingOnly() {
        return membertypeChangeRepository.findByRequestAdminStatusCode_Code("PENDING").stream()
                .map(this::toDto)
                .toList();
    }

    private MembertypeChangeResponseDto toDto(MembertypeChange entity) {
        // 상태코드 → 숫자 변환 (예시, 코드/DB에 맞게 보정)
        int statusCode = 0;
        String code = entity.getRequestAdminStatusCode().getCode();
        // "PENDING": 0, "REJECTED": 1, "APPROVED": 2 (예시)
        if ("PENDING".equals(code)) statusCode = 0;
        else if ("REJECTED".equals(code)) statusCode = 1;
        else if ("APPROVED".equals(code)) statusCode = 2;

        // 한글 역할명 변환
        String requestedMemberTypeName = switch (entity.getRequestedMemberTypeCode().getCode()) {
            case "EMPLOYEE" -> "기업회원";
            case "HUNTER" -> "컨설턴트";
            case "GENERAL" -> "일반회원";
            default -> entity.getRequestedMemberTypeCode().getCode();
        };

        return MembertypeChangeResponseDto.builder()
                .requestId(entity.getMembertypeChangeId())
                .userEmail(entity.getUser().getEmail())
                .userNickname(entity.getUser().getNickname())
//                .userName(entity.getUser().getName()) // User 엔티티에 getName()이 있다면
                .currentMemberTypeCode(entity.getCurrentMemberTypeCode().getCode())
                .requestedMemberTypeCode(entity.getRequestedMemberTypeCode().getCode())
                .requestedMemberTypeName(requestedMemberTypeName)
                .title(entity.getTitle())
                .requestStatus(entity.getRequestAdminStatusCode().getCode())
                .requestStatusCode(statusCode)
                .attachmentUrl(entity.getAttachmentUrl())
                .requestedAt(entity.getRequestedAt())
                .build();
    }


    @Transactional(readOnly = true)
    public MembertypeChangeDetailResponseDto getDetail(Integer requestId) {
        MembertypeChange entity = membertypeChangeRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청서를 찾을 수 없습니다."));

        return MembertypeChangeDetailResponseDto.builder()
                .requestId(entity.getMembertypeChangeId())
                .userEmail(entity.getUser().getEmail())
                .userNickname(entity.getUser().getNickname())
                .currentMemberTypeCode(entity.getCurrentMemberTypeCode().getCode())
                .requestedMemberTypeCode(entity.getRequestedMemberTypeCode().getCode())
                .title(entity.getTitle())
                .reason(entity.getReason())
                .content(entity.getContent())
                .referenceLink(entity.getReferenceLink())
                .attachmentUrl(entity.getAttachmentUrl())
                .requestStatus(entity.getRequestAdminStatusCode().getCode())
                .requestedAt(entity.getRequestedAt())
                .processedAt(entity.getProcessedAt())
                .build();
    }

}

