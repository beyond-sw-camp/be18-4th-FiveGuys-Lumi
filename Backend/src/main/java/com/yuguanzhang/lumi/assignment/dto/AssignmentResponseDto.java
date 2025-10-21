package com.yuguanzhang.lumi.assignment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuguanzhang.lumi.assignment.entity.Assignment;
import com.yuguanzhang.lumi.file.dto.FileUploadResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class AssignmentResponseDto {

    private final Long assignmentId;

    private final String title;

    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime deadlineAt;

    private final boolean isEvaluation;

    private final boolean isSubmission;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime evaluationDeadlineAt;

    private final List<FileUploadResponseDto> files;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public static AssignmentResponseDto fromEntity(Assignment assignment,
                                                   List<FileUploadResponseDto> files) {
        return AssignmentResponseDto.builder()
                                    .assignmentId(assignment.getAssignmentId())
                                    .title(assignment.getTitle())
                                    .content(assignment.getContent())
                                    .deadlineAt(assignment.getDeadlineAt())
                                    .isEvaluation(assignment.isEvaluation())
                                    .isSubmission(assignment.isSubmission())
                                    .evaluationDeadlineAt(assignment.getEvaluationDeadlineAt())
                                    .createdAt(assignment.getCreatedAt())
                                    .updatedAt(assignment.getUpdatedAt())
                                    .files(files)
                                    .build();
    }
}
