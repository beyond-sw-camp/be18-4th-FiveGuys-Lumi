package com.yuguanzhang.lumi.todo.dto;


import com.yuguanzhang.lumi.todo.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class TodoResponseDto {
    private final Long todoId;
    private final String description;
    private final boolean status;
    private final LocalDate dueDate;

    public static TodoResponseDto fromEntity(Todo todo) {
        return TodoResponseDto.builder()
                              .todoId(todo.getTodoId())
                              .description(todo.getDescription())
                              .status(todo.getStatus())
                              .dueDate(todo.getDueDate())
                              .build();
    }
}
