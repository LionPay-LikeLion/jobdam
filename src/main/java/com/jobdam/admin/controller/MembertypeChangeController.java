package com.jobdam.admin.controller;

import com.jobdam.admin.dto.MembertypeChangeDetailResponseDto;
import com.jobdam.admin.dto.MembertypeChangeProcessDto;
import com.jobdam.admin.dto.MembertypeChangeRequestDto;
import com.jobdam.admin.dto.MembertypeChangeResponseDto;
import com.jobdam.admin.service.MembertypeChangeService;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/membertype-change")
public class MembertypeChangeController {

    private final MembertypeChangeService membertypeChangeService;

    @PostMapping
    public ResponseEntity<Void> requestChange(
            @RequestBody MembertypeChangeRequestDto dto,
            @RequestParam Integer userId
    ) {

        membertypeChangeService.createRequest(userId, dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MembertypeChangeResponseDto>> getAllRequests() {
        return ResponseEntity.ok(membertypeChangeService.getAll());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<MembertypeChangeResponseDto>> getPendingRequests() {
        return ResponseEntity.ok(membertypeChangeService.getPendingOnly());
    }

    @PatchMapping("/{requestId}")
    public ResponseEntity<Void> processRequest(
            @PathVariable Integer requestId,
            @RequestBody MembertypeChangeProcessDto dto
    ) {
        membertypeChangeService.processRequest(requestId, dto.getStatusCode());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<MembertypeChangeDetailResponseDto> getDetail(@PathVariable Integer requestId) {
        return ResponseEntity.ok(membertypeChangeService.getDetail(requestId));
    }
}
