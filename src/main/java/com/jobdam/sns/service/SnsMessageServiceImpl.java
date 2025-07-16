package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsMessageRequestDto;
import com.jobdam.sns.dto.SnsMessageResponseDto;
import com.jobdam.sns.entity.SnsMessage;
import com.jobdam.sns.mapper.SnsMessageMapper;
import com.jobdam.sns.repository.SnsMessageRepository;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SnsMessageServiceImpl implements SnsMessageService {

    private final SnsMessageRepository snsMessageRepository;
    private final UserRepository userRepository;

    @Override
    public void sendMessage(Long senderId, SnsMessageRequestDto requestDto) {
        SnsMessage message = new SnsMessage();
        message.setSenderId(senderId);
        message.setReceiverId(requestDto.getReceiverId());
        message.setContent(requestDto.getContent());
        snsMessageRepository.save(message);
    }

    @Override
    public List<SnsMessageResponseDto> getConversation(Long userId1, Long userId2) {
        return snsMessageRepository
                .findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAt(
                        userId1, userId2, userId1, userId2)
                .stream()
                .map(SnsMessageMapper::toDto)
                .collect(Collectors.toList());
    }
}