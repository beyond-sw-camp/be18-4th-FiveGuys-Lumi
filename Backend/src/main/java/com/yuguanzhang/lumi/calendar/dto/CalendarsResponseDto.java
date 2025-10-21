package com.yuguanzhang.lumi.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuguanzhang.lumi.assignment.entity.Assignment;
import com.yuguanzhang.lumi.course.entity.Course;
import com.yuguanzhang.lumi.role.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CalendarsResponseDto {
    private final Long channelId;
    private final String channelName;
    private final Long entityId;
    private final String entityType;
    private final RoleName roleName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime endDate;

    public static CalendarsResponseDto fromCourse(Course course, RoleName roleName) {
        return CalendarsResponseDto.builder()
                                   .channelId(course.getChannelUser()
                                                    .getChannel()
                                                    .getChannelId())
                                   .channelName(course.getChannelUser()
                                                      .getChannel()
                                                      .getName())
                                   .entityId(course.getCourseId())
                                   .entityType("COURSE")
                                   .roleName(roleName)
                                   .startDate(course.getStartDate())
                                   .endDate(course.getEndDate())
                                   .build();
    }

    public static CalendarsResponseDto fromAssignment(Assignment assignment, RoleName roleName) {
        return CalendarsResponseDto.builder()
                                   .channelId(assignment.getChannelUser()
                                                        .getChannel()
                                                        .getChannelId())
                                   .channelName(assignment.getChannelUser()
                                                          .getChannel()
                                                          .getName())
                                   .entityId(assignment.getAssignmentId())
                                   .entityType("ASSIGNMENT")
                                   .roleName(roleName)
                                   .startDate(assignment.getUpdatedAt())
                                   .endDate(assignment.getDeadlineAt())
                                   .build();
    }

    public static CalendarsResponseDto fromEvaluation(Assignment evaluation, RoleName roleName) {
        return CalendarsResponseDto.builder()
                                   .channelId(evaluation.getChannelUser()
                                                        .getChannel()
                                                        .getChannelId())
                                   .channelName(evaluation.getChannelUser()
                                                          .getChannel()
                                                          .getName())
                                   .entityId(evaluation.getAssignmentId())
                                   .entityType("EVALUATION")
                                   .roleName(roleName)
                                   .startDate(evaluation.getDeadlineAt()
                                                        .plusDays(1))
                                   .endDate(evaluation.getEvaluationDeadlineAt())
                                   .build();
    }
}
