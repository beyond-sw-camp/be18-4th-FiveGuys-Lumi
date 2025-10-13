package com.yuguanzhang.lumi.assignment.dto;

import com.yuguanzhang.lumi.assignment.entity.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class EvaluationResponseDto {

    private Long evaluationId;

    private Integer grade;

    private String feedback;

    private Long submissionId;

    public static EvaluationResponseDto fromEntity(Evaluation evaluation) {
        return EvaluationResponseDto.builder()
                                    .evaluationId(evaluation.getEvaluationId())
                                    .grade(evaluation.getGrade())
                                    .feedback(evaluation.getFeedback())
                                    .submissionId(evaluation.getSubmission()
                                                            .getSubmissionId())
                                    .build();
    }
}
