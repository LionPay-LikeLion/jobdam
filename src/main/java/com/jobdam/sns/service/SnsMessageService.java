package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsMessageRequestDto;
import com.jobdam.sns.dto.SnsMessageResponseDto;

import java.util.List;

public interface SnsMessageService {

    void sendMessage(Integer senderId, SnsMessageRequestDto requestDto);

    List<SnsMessageResponseDto> getConversation(Long userId1, Long userId2);
}
