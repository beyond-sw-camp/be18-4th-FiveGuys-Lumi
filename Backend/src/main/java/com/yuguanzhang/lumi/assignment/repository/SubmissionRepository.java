package com.yuguanzhang.lumi.assignment.repository;

import com.yuguanzhang.lumi.assignment.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    boolean existsByAssignment_AssignmentIdAndChannelUser_ChannelUserId(Long assignmentAssignmentId,
                                                                        Long channelUserId);

    Optional<Submission> findByAssignment_AssignmentId(Long assignmentId);
}
