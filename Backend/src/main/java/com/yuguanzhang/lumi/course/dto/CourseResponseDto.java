package com.yuguanzhang.lumi.course.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuguanzhang.lumi.course.entity.Course;
import com.yuguanzhang.lumi.course.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class CourseResponseDto {
    private final Long courseId;
    private final Long channelId;
    private final String channelName;
    private final String location;
    private final StatusType statusType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime endDate;

    public static CourseResponseDto fromEntity(Course course) {
        return CourseResponseDto.builder()
                                .courseId(course.getCourseId())
                                .channelId(course.getChannelUser()
                                                 .getChannel()
                                                 .getChannelId())
                                .channelName(course.getChannelUser()
                                                   .getChannel()
                                                   .getName())
                                .startDate(course.getStartDate())
                                .endDate(course.getEndDate())
                                .location(course.getLocation())
                                .statusType(course.getStatusType())
                                .build();
    }
}
