package com.yuguanzhang.lumi.calendar.controller;

import com.yuguanzhang.lumi.calendar.dto.CalendarResponseDto;
import com.yuguanzhang.lumi.calendar.dto.CalendarsResponseDto;
import com.yuguanzhang.lumi.calendar.service.CalendarService;
import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.user.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
@Slf4j
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping
    public ResponseEntity<BaseResponseDto<CalendarsResponseDto>> getCalendars(
            @AuthenticationPrincipal UserDetailsDto user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        List<CalendarsResponseDto> course = calendarService.getCalendars(user.getUser()
                                                                             .getUserId(),
                                                                         startDate, endDate);

        BaseResponseDto<CalendarsResponseDto> response = BaseResponseDto.of(HttpStatus.OK, course);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/{date}")
    public ResponseEntity<BaseResponseDto<CalendarResponseDto>> getCalendar(
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dueDate) {

        List<CalendarResponseDto> course = calendarService.getCalendarsByDate(user.getUser()
                                                                                  .getUserId(),
                                                                              dueDate);


        BaseResponseDto<CalendarResponseDto> response = BaseResponseDto.of(HttpStatus.OK, course);


        return ResponseEntity.ok(response);
    }
}
