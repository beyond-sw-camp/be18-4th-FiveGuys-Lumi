package com.yuguanzhang.lumi.course.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.course.entity.Course;
import com.yuguanzhang.lumi.course.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class CourseRequestDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "시작 날짜를 입력해주세요.")
    private LocalDateTime startDate;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "종료 날짜는 입력해주세요.")
    private LocalDateTime endDate;

    private String location;

    private StatusType statusType;

    public Course toEntity(ChannelUser channelUser) {
        return Course.builder()
                     .channelUser(channelUser)
                     .startDate(this.startDate)
                     .endDate(this.endDate)
                     .location(this.location)
                     .statusType(this.statusType != null ? this.statusType : StatusType.SCHEDULED)
                     .build();

    }
}
