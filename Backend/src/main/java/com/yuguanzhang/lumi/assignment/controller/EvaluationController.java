package com.yuguanzhang.lumi.assignment.controller;

import com.yuguanzhang.lumi.assignment.dto.EvaluationRequestDto;
import com.yuguanzhang.lumi.assignment.dto.EvaluationResponseDto;
import com.yuguanzhang.lumi.assignment.service.EvaluationService;
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
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping("/submissions/{submission_id}/evaluation")
    public ResponseEntity<BaseResponseDto<EvaluationResponseDto>> createEvaluation(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("submission_id") Long submissionId,
            @AuthenticationPrincipal UserDetailsDto user,
            @Valid @RequestBody EvaluationRequestDto evaluationRequestDto) {

        EvaluationResponseDto responseDto =
                evaluationService.createEvaluation(channelId, submissionId, user.getUser(),
                                                   evaluationRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(BaseResponseDto.of(HttpStatus.CREATED, responseDto));
    }

    @GetMapping("/submissions/{submission_id}/evaluation")
    public ResponseEntity<BaseResponseDto<EvaluationResponseDto>> getEvaluation(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("submission_id") Long submissionId,
            @AuthenticationPrincipal UserDetailsDto user) {

        EvaluationResponseDto responseDto =
                evaluationService.getEvaluation(channelId, submissionId, user.getUser());

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, responseDto));
    }

    @PutMapping("/submissions/{submission_id}/evaluation")
    public ResponseEntity<BaseResponseDto<EvaluationResponseDto>> updateEvaluation(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("submission_id") Long submissionId,
            @AuthenticationPrincipal UserDetailsDto user,
            @Valid @RequestBody EvaluationRequestDto evaluationRequestDto) {

        EvaluationResponseDto responseDto =
                evaluationService.updateEvaluation(channelId, submissionId, user.getUser(),
                                                   evaluationRequestDto);

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, responseDto));
    }

    @DeleteMapping("/submissions/{submission_id}/evaluation")
    public ResponseEntity<BaseResponseDto<Void>> deleteEvaluation(
            @PathVariable("channel_id") Long channelId,
            @PathVariable("submission_id") Long submissionId,
            @AuthenticationPrincipal UserDetailsDto user) {

        evaluationService.deleteEvaluation(channelId, submissionId, user.getUser());

        return ResponseEntity.ok(BaseResponseDto.of(HttpStatus.OK, null));
    }
}
