package com.yuguanzhang.lumi.grade.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuguanzhang.lumi.grade.Entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class GradeResponseDto {

    private final Long gradeId;

    private final String title;

    private String category;

    private final int grade;

    private final LocalDate date;

    private final Long channelUserId;


    public static GradeResponseDto fromEntity(Grade grade) {
        return GradeResponseDto.builder()
                               .gradeId(grade.getGradeId())
                               .title(grade.getTitle())
                               .category(grade.getCategory())
                               .grade(grade.getGrades())
                               .date(grade.getDate())
                               .channelUserId(grade.getChannelUser()
                                                   .getChannelUserId())
                               .build();
    }
}
