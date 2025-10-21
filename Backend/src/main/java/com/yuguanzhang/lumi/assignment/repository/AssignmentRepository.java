package com.yuguanzhang.lumi.assignment.repository;

import com.yuguanzhang.lumi.assignment.entity.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    Page<Assignment> findByChannelUser_Channel_ChannelId(Long channelId, Pageable pageable);

    @Query("SELECT a FROM Assignment a " + "WHERE a.channelUser.channel.channelId IN :channelIds " + "AND (a.deadlineAt BETWEEN :startDate AND :endDate " + "     " + "OR (a.isEvaluation = true AND a.evaluationDeadlineAt BETWEEN :startDate AND :endDate))")
    List<Assignment> findAssignmentsInDateRange(@Param("channelIds") List<Long> channelIds,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

}
