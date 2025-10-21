package com.yuguanzhang.lumi.grade.dto;

import com.yuguanzhang.lumi.grade.Entity.Grade;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class GradeRequestDto {

    private String title;

    private String category;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer grades;

    private LocalDate date;

    public Grade toEntity() {

        return Grade.builder()
                    .title(this.title)
                    .category(this.category)
                    .grades(this.grades)
                    .date(this.date)
                    .build();
    }

}
