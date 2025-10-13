package com.yuguanzhang.lumi.course.dto;

import com.yuguanzhang.lumi.course.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CourseStatusUpdateRequestDto {
    private StatusType statusType;
}
