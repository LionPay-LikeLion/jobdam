package com.jobdam.user.repository;

import com.jobdam.user.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderId(String providerId);

    Optional<User> findByProviderIdAndProviderType(String providerId, String providerType);

    Optional<User> findByEmailAndMemberTypeCodeId(String email, Integer memberTypeCodeId);

    List<User> findByNicknameContainingIgnoreCaseAndUserIdNot(String keyword, Integer excludeUserId, PageRequest of);
}