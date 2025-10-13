package com.yuguanzhang.lumi.assignment.dto;

import com.yuguanzhang.lumi.assignment.entity.Submission;
import com.yuguanzhang.lumi.file.dto.FileUploadResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class SubmissionResponseDto {

    private Long submissionId;

    private String title;

    private String description;

    private Long assignmentId;

    private List<FileUploadResponseDto> files;

    public static SubmissionResponseDto fromEntity(Submission submission,
                                                   List<FileUploadResponseDto> files) {
        return SubmissionResponseDto.builder()
                                    .submissionId(submission.getSubmissionId())
                                    .title(submission.getTitle())
                                    .description(submission.getDescription())
                                    .assignmentId(submission.getAssignment()
                                                            .getAssignmentId())
                                    .files(files)
                                    .build();
    }
}
