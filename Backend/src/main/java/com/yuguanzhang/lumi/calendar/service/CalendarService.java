package com.yuguanzhang.lumi.calendar.service;

import com.yuguanzhang.lumi.calendar.dto.CalendarResponseDto;
import com.yuguanzhang.lumi.calendar.dto.CalendarsResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CalendarService {
    List<CalendarsResponseDto> getCalendars(UUID userId, LocalDate startDate, LocalDate endDate);


    List<CalendarResponseDto> getCalendarsByDate(UUID userId, LocalDate dueDate);
}
