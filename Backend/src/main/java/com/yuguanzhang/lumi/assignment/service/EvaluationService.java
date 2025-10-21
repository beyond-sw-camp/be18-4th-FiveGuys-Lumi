package com.yuguanzhang.lumi.assignment.service;


import com.yuguanzhang.lumi.assignment.dto.EvaluationRequestDto;
import com.yuguanzhang.lumi.assignment.dto.EvaluationResponseDto;
import com.yuguanzhang.lumi.user.entity.User;

public interface EvaluationService {
    EvaluationResponseDto createEvaluation(Long channelId, Long submissionId, User user,
                                           EvaluationRequestDto dto);

    EvaluationResponseDto getEvaluation(Long channelId, Long submissionId, User user);

    EvaluationResponseDto updateEvaluation(Long channelId, Long submissionId, User user,
                                           EvaluationRequestDto dto);

    void deleteEvaluation(Long channelId, Long submissionId, User user);
}
