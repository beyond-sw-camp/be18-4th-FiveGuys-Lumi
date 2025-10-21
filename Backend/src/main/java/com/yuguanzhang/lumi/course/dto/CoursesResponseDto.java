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
public class CoursesResponseDto {
    private final Long courseId;
    private final Long channelId;
    private final String channelName;
    private final StatusType statusType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime endDate;

    public static CoursesResponseDto fromEntity(Course course) {
        return CoursesResponseDto.builder()
                                 .courseId(course.getCourseId())
                                 .channelId(course.getChannelUser()
                                                  .getChannel()
                                                  .getChannelId())
                                 .channelName(course.getChannelUser()
                                                    .getChannel()
                                                    .getName())
                                 .statusType(course.getStatusType())
                                 .startDate(course.getStartDate())
                                 .endDate(course.getEndDate())
                                 .build();
    }
}
