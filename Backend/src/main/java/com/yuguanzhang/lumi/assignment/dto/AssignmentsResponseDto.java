package com.yuguanzhang.lumi.assignment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuguanzhang.lumi.assignment.entity.Assignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class AssignmentsResponseDto {

    private final Long assignmentId;

    private final String title;

    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime deadlineAt;

    private final boolean isEvaluation;

    private final boolean isSubmission;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime evaluationDeadlineAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public static AssignmentsResponseDto fromEntity(Assignment assignment) {
        return AssignmentsResponseDto.builder()
                                     .assignmentId(assignment.getAssignmentId())
                                     .title(assignment.getTitle())
                                     .content(assignment.getContent())
                                     .deadlineAt(assignment.getDeadlineAt())
                                     .isEvaluation(assignment.isEvaluation())
                                     .isSubmission(assignment.isSubmission())
                                     .evaluationDeadlineAt(assignment.getEvaluationDeadlineAt())
                                     .createdAt(assignment.getCreatedAt())
                                     .updatedAt(assignment.getUpdatedAt())
                                     .build();
    }
}
