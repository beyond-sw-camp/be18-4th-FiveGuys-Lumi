package com.yuguanzhang.lumi.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeUtil {
    private DateTimeUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static LocalDateTime getStartOfDay(LocalDate date) {
        return (date == null ? LocalDate.now() : date).atStartOfDay();
    }

    public static LocalDateTime getEndOfDay(LocalDate date) {
        return (date == null ? LocalDate.now() : date).atTime(23, 59, 59);
    }
}
