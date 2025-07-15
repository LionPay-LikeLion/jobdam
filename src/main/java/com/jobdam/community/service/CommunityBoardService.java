package com.jobdam.community.service;

import com.jobdam.code.entity.BoardStatusCode;
import com.jobdam.code.entity.BoardTypeCode;
import com.jobdam.code.repository.BoardTypeCodeRepository;
import com.jobdam.code.repository.BoardStatusCodeRepository;
import com.jobdam.community.dto.CommunityBoardCreateRequestDto;
import com.jobdam.community.dto.CommunityBoardListResponseDto;
import com.jobdam.community.entity.Community;
import com.jobdam.community.entity.CommunityBoard;
import com.jobdam.community.repository.CommunityBoardRepository;
import com.jobdam.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityBoardService {

    private final CommunityBoardRepository communityBoardRepository;
    private final BoardTypeCodeRepository boardTypeCodeRepository;
    private final BoardStatusCodeRepository boardStatusCodeRepository;
    private final CommunityRepository communityRepository;

    @Transactional
    public Integer createBoard(CommunityBoardCreateRequestDto dto, Integer communityId) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("커뮤니티를 찾을 수 없습니다."));


        BoardTypeCode boardTypeCode = boardTypeCodeRepository.findById(dto.getBoardTypeCodeId())
                .orElseThrow(() -> new RuntimeException("게시판 타입을 찾을 수 없습니다."));


        CommunityBoard board = new CommunityBoard();
        board.setName(dto.getName());
        board.setDescription(dto.getDescription());
        board.setCommunityId(communityId);
        board.setBoardTypeCodeId(dto.getBoardTypeCodeId()); // id로 저장
        board.setBoardStatusCodeId(1); // (기본값)

        communityBoardRepository.save(board);

        return board.getCommunityBoardId();
    }

    @Transactional(readOnly = true)
    public List<CommunityBoardListResponseDto> getAllBoardsByCommunityId(Integer communityId) {
        List<CommunityBoard> boards = communityBoardRepository.findAllByCommunityIdOrderByCreatedAtDesc(communityId);

        return boards.stream()
                .map(b -> CommunityBoardListResponseDto.builder()
                        .communityBoardId(b.getCommunityBoardId())
                        .name(b.getName())
                        .description(b.getDescription())
                        .boardTypeCode(
                                boardTypeCodeRepository.findById(b.getBoardTypeCodeId())
                                        .map(BoardTypeCode::getCode)
                                        .orElse(null)
                        )
                        .boardStatusCode(
                                boardStatusCodeRepository.findById(b.getBoardStatusCodeId())
                                        .map(BoardStatusCode::getCode)
                                        .orElse(null)
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }

}
