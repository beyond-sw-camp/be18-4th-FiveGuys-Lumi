package com.yuguanzhang.lumi.common.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class LocalDateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public LocalDateTime getStartDateTime() {
        return startDate.atStartOfDay();
    }

    public LocalDateTime getEndDateTime() {
        return endDate.atTime(23, 59, 59);
    }


}
