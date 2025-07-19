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

        String fileUrl = null;
        if (dto.getAttachment() != null && !dto.getAttachment().isEmpty()) {
            String rootPath = System.getProperty("user.dir");
            String uploadDir = rootPath + File.separator + "uploads" + File.separator + userId + File.separator;
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID() + "_" + dto.getAttachment().getOriginalFilename();
            File dest = new File(uploadDir + fileName);

            try {
                dto.getAttachment().transferTo(dest);
                fileUrl = "/uploads/" + userId + "/" + fileName;
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
            }
        }



        MembertypeChange entity = MembertypeChange.builder()

                .userId(userId)
                .currentMemberTypeCodeId(currentType.getMemberTypeCodeId())
                .requestedMemberTypeCodeId(requestedType.getMemberTypeCodeId())
                .title(dto.getTitle())
                .reason(dto.getReason())
                .content(dto.getContent())
                .referenceLink(dto.getReferenceLink())
                .attachmentUrl(fileUrl)
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
        return MembertypeChangeResponseDto.builder()
                .requestId(entity.getMembertypeChangeId())
                .userEmail(entity.getUser().getEmail())
                .userNickname(entity.getUser().getNickname())
                .currentMemberTypeCode(entity.getCurrentMemberTypeCode().getCode())
                .requestedMemberTypeCode(entity.getRequestedMemberTypeCode().getCode())
                .title(entity.getTitle())
                .requestStatus(entity.getRequestAdminStatusCode().getCode())
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

