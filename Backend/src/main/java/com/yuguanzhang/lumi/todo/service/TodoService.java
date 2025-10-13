package com.yuguanzhang.lumi.todo.service;

import com.yuguanzhang.lumi.todo.dto.TodoRequestDto;
import com.yuguanzhang.lumi.todo.dto.TodoResponseDto;
import com.yuguanzhang.lumi.todo.dto.TodoUpdateRequestDto;
import com.yuguanzhang.lumi.todo.dto.TodosResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TodoService {
    List<TodosResponseDto> getTodos(UUID userId, LocalDate startDate, LocalDate endDate);

    List<TodoResponseDto> getTodosByDate(UUID userId, LocalDate dueDate);

    TodoResponseDto createTodo(UUID userId, TodoRequestDto request);

    TodoResponseDto updateTodo(UUID userId, TodoUpdateRequestDto request, Long todoId);

    void deleteTodo(UUID userId, Long todoId);

}
