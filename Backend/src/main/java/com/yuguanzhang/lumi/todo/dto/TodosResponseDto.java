package com.yuguanzhang.lumi.todo.dto;

import com.yuguanzhang.lumi.todo.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class TodosResponseDto {
    private final int incompleteCount;
    private final boolean allCompleted;
    private final LocalDate dueDate;

    public static TodosResponseDto fromEntity(LocalDate dueDate, List<Todo> todos) {

        int incompleteCount = (int) todos.stream()
                                         .filter(todo -> !todo.getStatus())
                                         .count();
        boolean allCompleted = !todos.isEmpty() && incompleteCount == 0;


        return TodosResponseDto.builder()
                               .incompleteCount(incompleteCount)
                               .allCompleted(allCompleted)
                               .dueDate(dueDate)
                               .build();
    }
}
