package com.yuguanzhang.lumi.common.util;

import java.time.LocalDate;

public class DateRangeUtil {
    private DateRangeUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static LocalDateRange getMonthlyRange(LocalDate startDate, LocalDate endDate) {
        startDate = (startDate == null) ?
                LocalDate.now()
                         .withDayOfMonth(1) :
                startDate;

        endDate = (endDate == null) ?
                LocalDate.now()
                         .withDayOfMonth((startDate.lengthOfMonth())) :
                endDate;

        return new LocalDateRange(startDate, endDate);
    }

    public static LocalDateRange of(LocalDate startDate, LocalDate endDate) {
        return new LocalDateRange(startDate, endDate);
    }
}
