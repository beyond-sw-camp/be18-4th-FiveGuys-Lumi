package com.yuguanzhang.lumi.assignment.controller;

import com.yuguanzhang.lumi.assignment.dto.SubmissionRequestDto;
import com.yuguanzhang.lumi.assignment.dto.SubmissionResponseDto;
import com.yuguanzhang.lumi.assignment.service.SubmissionService;
import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.user.dto.UserDetailsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/channels/{channel_id}/assignments")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping("/{assignment_id}/submissions")
    public ResponseEntity<BaseResponseDto<SubmissionResponseDto>> createSubmission(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("assignment_id") Long assignmentId,
            @AuthenticationPrincipal UserDetailsDto user,
            @Valid @RequestBody SubmissionRequestDto submissionRequestDto) {

        SubmissionResponseDto submissionResponseDto =
                submissionService.createSubmission(channelId, assignmentId, user.getUser(),
                                                   submissionRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(BaseResponseDto.of(HttpStatus.CREATED, submissionResponseDto));
    }

    @GetMapping("/{assignment_id}/submissions")
    public ResponseEntity<BaseResponseDto<SubmissionResponseDto>> getSubmission(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("assignment_id") Long assignmentId,
            @AuthenticationPrincipal UserDetailsDto user) {

        SubmissionResponseDto submissionResponseDto =
                submissionService.getSubmission(channelId, assignmentId, user.getUser());

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, submissionResponseDto));
    }

    @PutMapping("/{assignment_id}/submissions/{submission_id}")
    public ResponseEntity<BaseResponseDto<SubmissionResponseDto>> updateSubmission(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("assignment_id") Long assignmentId,
            @PathVariable("submission_id") Long submissionId,
            @AuthenticationPrincipal UserDetailsDto user,
            @Valid @RequestBody SubmissionRequestDto submissionRequestDto) {
        SubmissionResponseDto submissionResponseDto =
                submissionService.updateSubmission(channelId, assignmentId, submissionId,
                                                   user.getUser(), submissionRequestDto);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, submissionResponseDto));
    }

    @DeleteMapping("/{assignment_id}/submissions/{submission_id}")
    public ResponseEntity<BaseResponseDto<Void>> deleteSubmission(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("assignment_id") Long assignmentId,
            @PathVariable("submission_id") Long submissionId,
            @AuthenticationPrincipal UserDetailsDto user) {

        submissionService.deleteSubmission(channelId, assignmentId, submissionId, user.getUser());

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, null));
    }
}
