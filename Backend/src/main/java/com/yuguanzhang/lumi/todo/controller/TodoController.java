package com.yuguanzhang.lumi.todo.controller;

import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.todo.dto.TodoRequestDto;
import com.yuguanzhang.lumi.todo.dto.TodoResponseDto;
import com.yuguanzhang.lumi.todo.dto.TodoUpdateRequestDto;
import com.yuguanzhang.lumi.todo.dto.TodosResponseDto;
import com.yuguanzhang.lumi.todo.service.TodoService;
import com.yuguanzhang.lumi.user.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<BaseResponseDto<TodosResponseDto>> getTodos(
            @AuthenticationPrincipal UserDetailsDto user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        List<TodosResponseDto> todos = todoService.getTodos(user.getUser()
                                                                .getUserId(), startDate, endDate);

        BaseResponseDto<TodosResponseDto> response = BaseResponseDto.of(HttpStatus.OK, todos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{date}")
    public ResponseEntity<BaseResponseDto<TodoResponseDto>> getTodosByDate(
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dueDate) {

        List<TodoResponseDto> todos = todoService.getTodosByDate(user.getUser()
                                                                     .getUserId(), dueDate);

        BaseResponseDto<TodoResponseDto> response = BaseResponseDto.of(HttpStatus.OK, todos);

        return ResponseEntity.ok(response);
    }



    @PostMapping
    public ResponseEntity<BaseResponseDto<TodoResponseDto>> createTodo(
            @AuthenticationPrincipal UserDetailsDto user, @RequestBody TodoRequestDto request) {

        TodoResponseDto todo = todoService.createTodo(user.getUser()
                                                          .getUserId(), request);

        BaseResponseDto<TodoResponseDto> response = BaseResponseDto.of(HttpStatus.CREATED, todo);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    @PatchMapping("/{todo_id}")
    public ResponseEntity<BaseResponseDto<TodoResponseDto>> updateTodo(
            @AuthenticationPrincipal UserDetailsDto user, @RequestBody TodoUpdateRequestDto request,
            @PathVariable("todo_id") Long todoId) {

        TodoResponseDto todo = todoService.updateTodo(user.getUser()
                                                          .getUserId(), request, todoId);

        BaseResponseDto<TodoResponseDto> response = BaseResponseDto.of(HttpStatus.OK, todo);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{todo_id}")
    public ResponseEntity<BaseResponseDto<Void>> deleteTodo(
            @AuthenticationPrincipal UserDetailsDto user, @PathVariable("todo_id") Long todoId) {

        todoService.deleteTodo(user.getUser()
                                   .getUserId(), todoId);

        BaseResponseDto<Void> response = BaseResponseDto.of(HttpStatus.OK, null);

        return ResponseEntity.ok(response);
    }

}
