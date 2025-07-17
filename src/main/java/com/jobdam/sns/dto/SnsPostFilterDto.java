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

    private String memberType;
    private String sort = "latest";
}
