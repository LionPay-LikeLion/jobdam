package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsMessageBoxResponseDto;

import java.util.List;

public interface SnsMessageBoxService {

    List<SnsMessageBoxResponseDto> getMessageBoxes(Long userId);
}
