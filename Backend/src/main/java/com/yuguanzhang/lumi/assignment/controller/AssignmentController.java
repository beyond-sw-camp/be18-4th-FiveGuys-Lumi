package com.yuguanzhang.lumi.assignment.controller;

import com.yuguanzhang.lumi.assignment.dto.AssignmentRequestDto;
import com.yuguanzhang.lumi.assignment.dto.AssignmentResponseDto;
import com.yuguanzhang.lumi.assignment.dto.AssignmentsResponseDto;
import com.yuguanzhang.lumi.assignment.service.AssignmentService;
import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.common.dto.PageResponseDto;
import com.yuguanzhang.lumi.user.dto.UserDetailsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping("/{channel_id}/assignments")
    public ResponseEntity<BaseResponseDto<AssignmentResponseDto>> createAssignment(
            @PathVariable("channel_id") Long channelId,
            @AuthenticationPrincipal UserDetailsDto user,
            @Valid @RequestBody AssignmentRequestDto assignmentRequestDto) {

        AssignmentResponseDto assignmentResponseDto =
                assignmentService.createAssignment(channelId, user.getUser(), assignmentRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(BaseResponseDto.of(HttpStatus.CREATED, assignmentResponseDto));
    }

    @GetMapping("/{channel_id}/assignments")
    public ResponseEntity<PageResponseDto<AssignmentsResponseDto>> getAssignments(
            @PathVariable("channel_id") Long channelId, Pageable pageable) {

        Page<AssignmentsResponseDto> assignments =
                assignmentService.getAssignments(channelId, pageable);

        return ResponseEntity.ok(PageResponseDto.page(HttpStatus.OK, assignments));

    }

    @GetMapping("/{channel_id}/assignments/{assignment_id}")
    public ResponseEntity<BaseResponseDto<AssignmentResponseDto>> getAssignment(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("assignment_id") Long assignmentId) {

        AssignmentResponseDto assignmentResponseDto = assignmentService.getAssignment(assignmentId);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, assignmentResponseDto));
    }

    @PutMapping("/{channel_id}/assignments/{assignment_id}")
    public ResponseEntity<BaseResponseDto<AssignmentResponseDto>> updateAssignment(
            @PathVariable("channel_id") Long channelId,
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("assignment_id") Long assignmentId,
            @Valid @RequestBody AssignmentRequestDto assignmentRequestDto) {

        AssignmentResponseDto assignmentResponseDto =
                assignmentService.updateAssignment(channelId, user.getUser(), assignmentId,
                                                   assignmentRequestDto);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, assignmentResponseDto));
    }

    @DeleteMapping("/{channel_id}/assignments/{assignment_id}")
    public ResponseEntity<BaseResponseDto<Void>> deleteAssignment(
            @PathVariable("channel_id") Long channelId,
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("assignment_id") Long assignmentId) {

        assignmentService.deleteAssignment(channelId, user.getUser(), assignmentId);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, null));
    }
}
