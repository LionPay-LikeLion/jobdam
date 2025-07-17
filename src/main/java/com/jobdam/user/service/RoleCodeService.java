package com.jobdam.user.service;

import com.jobdam.user.repository.RoleCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleCodeService {

    private final RoleCodeRepository roleCodeRepository;

    public String getRoleNameById(Integer roleCodeId) {
        return roleCodeRepository.findById(roleCodeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role code"))
                .getName();
    }
}
