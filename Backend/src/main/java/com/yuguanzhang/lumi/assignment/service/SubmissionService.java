package com.yuguanzhang.lumi.assignment.service;

import com.yuguanzhang.lumi.assignment.dto.SubmissionRequestDto;
import com.yuguanzhang.lumi.assignment.dto.SubmissionResponseDto;
import com.yuguanzhang.lumi.user.entity.User;

public interface SubmissionService {

    SubmissionResponseDto createSubmission(Long channelId, Long assignmentId, User user,
                                           SubmissionRequestDto submissionRequestDto);

    SubmissionResponseDto getSubmission(Long channelId, Long assignmentId, User user);

    SubmissionResponseDto updateSubmission(Long channelId, Long assignmentId, Long submissionId,
                                           User user, SubmissionRequestDto submissionRequestDto);

    void deleteSubmission(Long channelId, Long assignmentId, Long submissionId, User user);
}
