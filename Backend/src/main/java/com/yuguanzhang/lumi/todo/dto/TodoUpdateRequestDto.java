package com.yuguanzhang.lumi.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TodoUpdateRequestDto {

    @NotBlank
    private String description;

    private Boolean status;
}
