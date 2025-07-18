package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsMessageBoxResponseDto;
import com.jobdam.sns.repository.SnsMessageBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SnsMessageBoxServiceImpl implements SnsMessageBoxService {

    private final SnsMessageBoxRepository snsMessageBoxRepository;

    @Override
    public List<SnsMessageBoxResponseDto> getMessageBoxes(Integer userId) {
        return snsMessageBoxRepository.findMessageBoxesByUserId(userId);
    }
}
