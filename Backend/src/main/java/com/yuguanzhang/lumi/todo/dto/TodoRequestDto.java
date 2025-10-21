package com.yuguanzhang.lumi.todo.dto;

import com.yuguanzhang.lumi.todo.entity.Todo;
import com.yuguanzhang.lumi.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class TodoRequestDto {
    @NotBlank
    private String description;

    private LocalDate dueDate;

    public Todo toEntity(User user) {
        return Todo.builder()
                   .description(this.description)
                   .status(false)
                   .user(user)
                   .dueDate(this.dueDate)
                   .build();
    }
}
