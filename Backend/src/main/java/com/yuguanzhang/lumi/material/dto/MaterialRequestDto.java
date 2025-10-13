package com.yuguanzhang.lumi.material.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class MaterialRequestDto {

    @NotBlank
    private String title;

    private String content;

    private List<Long> fileIds;

}
