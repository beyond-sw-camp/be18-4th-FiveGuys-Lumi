package com.yuguanzhang.lumi.assignment.service;

import com.yuguanzhang.lumi.assignment.dto.AssignmentRequestDto;
import com.yuguanzhang.lumi.assignment.dto.AssignmentResponseDto;
import com.yuguanzhang.lumi.assignment.dto.AssignmentsResponseDto;
import com.yuguanzhang.lumi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssignmentService {
    AssignmentResponseDto createAssignment(Long channelId, User user,
                                           AssignmentRequestDto assignmentRequestDto);

    Page<AssignmentsResponseDto> getAssignments(Long channelId, Pageable pageable);

    AssignmentResponseDto getAssignment(Long assignmentId);

    AssignmentResponseDto updateAssignment(Long channelId, User user, Long assignmentId,
                                           AssignmentRequestDto assignmentRequestDto);

    void deleteAssignment(Long channelId, User user, Long assignmentId);

}
