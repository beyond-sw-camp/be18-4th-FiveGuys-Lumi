package com.yuguanzhang.lumi.assignment.service;

import com.yuguanzhang.lumi.assignment.dto.EvaluationRequestDto;
import com.yuguanzhang.lumi.assignment.dto.EvaluationResponseDto;
import com.yuguanzhang.lumi.assignment.entity.Assignment;
import com.yuguanzhang.lumi.assignment.entity.Evaluation;
import com.yuguanzhang.lumi.assignment.entity.Submission;
import com.yuguanzhang.lumi.assignment.repository.EvaluationRepository;
import com.yuguanzhang.lumi.assignment.repository.SubmissionRepository;
import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.common.service.RoleAuthorizationService;
import com.yuguanzhang.lumi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;

    private final SubmissionRepository submissionRepository;

    private final RoleAuthorizationService roleAuthorizationService;

    @Override
    public EvaluationResponseDto createEvaluation(Long channelId, Long submissionId, User user,
                                                  EvaluationRequestDto dto) {
        // 튜터 권한 검증
        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        Submission submission = submissionRepository.findById(submissionId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.SUBMISSION_NOT_FOUND));
        Assignment assignment = submission.getAssignment();

        if (!assignment.isEvaluation()) {
            throw new GlobalException(ExceptionMessage.EVALUATION_NOT_ALLOWED);
        }
        
        if (evaluationRepository.existsBySubmission_SubmissionId(submission.getSubmissionId())) {
            throw new GlobalException(ExceptionMessage.EVALUATION_ALREADY_EXISTS);
        }

        Evaluation evaluation = Evaluation.builder()
                                          .grade(dto.getGrade())
                                          .feedback(dto.getFeedback())
                                          .submission(submission)
                                          .build();

        Evaluation saved = evaluationRepository.save(evaluation);

        return EvaluationResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationResponseDto getEvaluation(Long channelId, Long submissionId, User user) {
        Submission submission = submissionRepository.findById(submissionId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.SUBMISSION_NOT_FOUND));

        Evaluation evaluation =
                evaluationRepository.findBySubmission_SubmissionId(submission.getSubmissionId())
                                    .orElseThrow(() -> new GlobalException(
                                            ExceptionMessage.EVALUATION_NOT_FOUND));
        return EvaluationResponseDto.fromEntity(evaluation);
    }

    @Override
    public EvaluationResponseDto updateEvaluation(Long channelId, Long submissionId, User user,
                                                  EvaluationRequestDto dto) {

        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        Submission submission = submissionRepository.findById(submissionId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.SUBMISSION_NOT_FOUND));

        Evaluation evaluation =
                evaluationRepository.findBySubmission_SubmissionId(submission.getSubmissionId())
                                    .orElseThrow(() -> new GlobalException(
                                            ExceptionMessage.EVALUATION_NOT_FOUND));

        evaluation.updateGrade(dto.getGrade());
        evaluation.updateFeedback(dto.getFeedback());

        return EvaluationResponseDto.fromEntity(evaluation);
    }

    @Override
    public void deleteEvaluation(Long channelId, Long submissionId, User user) {

        roleAuthorizationService.checkTutor(channelId, user.getUserId());

        Submission submission = submissionRepository.findById(submissionId)
                                                    .orElseThrow(() -> new GlobalException(
                                                            ExceptionMessage.SUBMISSION_NOT_FOUND));

        Evaluation evaluation =
                evaluationRepository.findBySubmission_SubmissionId(submission.getSubmissionId())
                                    .orElseThrow(() -> new GlobalException(
                                            ExceptionMessage.EVALUATION_NOT_FOUND));

        evaluationRepository.delete(evaluation);
    }
}
