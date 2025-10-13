package com.yuguanzhang.lumi.grade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GradeCategoryGroupDto {

    private String category;
    
    private List<GradeResponseDto> grades;
}
