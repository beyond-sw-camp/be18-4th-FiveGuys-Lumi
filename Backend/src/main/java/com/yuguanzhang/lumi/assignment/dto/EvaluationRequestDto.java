package com.yuguanzhang.lumi.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class EvaluationRequestDto {

    private Integer grade;

    private String feedback;
}
