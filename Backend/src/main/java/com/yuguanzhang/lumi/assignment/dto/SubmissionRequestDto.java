package com.yuguanzhang.lumi.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class SubmissionRequestDto {

    @NotBlank
    private String title;
    private String description;
    private List<Long> fileIds;
}
