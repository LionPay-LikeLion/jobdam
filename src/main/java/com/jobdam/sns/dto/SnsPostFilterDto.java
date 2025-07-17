package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnsPostFilterDto {

    // 작성자 유형 필터: GENERAL, HUNTER, EMPLOYEE
    private String memberType;

    // 정렬 기준: latest(기본), likes
    private String sort = "latest";
}
