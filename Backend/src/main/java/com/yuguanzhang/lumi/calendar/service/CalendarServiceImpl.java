package com.yuguanzhang.lumi.calendar.service;

import com.yuguanzhang.lumi.assignment.entity.Assignment;
import com.yuguanzhang.lumi.assignment.repository.AssignmentRepository;
import com.yuguanzhang.lumi.calendar.dto.CalendarResponseDto;
import com.yuguanzhang.lumi.calendar.dto.CalendarsResponseDto;
import com.yuguanzhang.lumi.channel.repository.ChannelUserRepository;
import com.yuguanzhang.lumi.common.util.DateRangeUtil;
import com.yuguanzhang.lumi.common.util.DateTimeUtil;
import com.yuguanzhang.lumi.common.util.LocalDateRange;
import com.yuguanzhang.lumi.course.entity.Course;
import com.yuguanzhang.lumi.course.respository.CourseRepository;
import com.yuguanzhang.lumi.role.entity.RoleName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarServiceImpl implements CalendarService {
    private final ChannelUserRepository channelUserRepository;
    private final CourseRepository courseRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CalendarsResponseDto> getCalendars(UUID userId, LocalDate startDate,
                                                   LocalDate endDate) {

        LocalDateRange range = DateRangeUtil.getMonthlyRange(startDate, endDate);
        LocalDateTime startDateTime = range.getStartDateTime();
        LocalDateTime endDateTime = range.getEndDateTime();

        // 1. 내가 속한 채널 Id들
        List<Long> channelIds = channelUserRepository.findChannelIdsByUserId(userId);

        // 2. 채널별 내 역할(RoleName) 맵 만들기
        Map<Long, RoleName> myRoles = channelUserRepository.findByUserUserId(userId)
                                                           .stream()
                                                           .collect(Collectors.toMap(
                                                                   cu -> cu.getChannel()
                                                                           .getChannelId(),
                                                                   cu -> cu.getRole()
                                                                           .getRoleName()));

        // 3. 과목/과제 가져오기
        List<Course> courses =
                courseRepository.findCoursesInDateRange(channelIds, startDateTime, endDateTime);
        List<Assignment> assignments =
                assignmentRepository.findAssignmentsInDateRange(channelIds, startDateTime,
                                                                endDateTime);

        // 4. DTO 변환 (내 역할 주입)
        List<CalendarsResponseDto> responseDtos = new ArrayList<>();

        courses.forEach(course -> {
            RoleName myRole = myRoles.get(course.getChannelUser()
                                                .getChannel()
                                                .getChannelId());
            responseDtos.add(CalendarsResponseDto.fromCourse(course, myRole));
        });

        assignments.forEach(assignment -> {
            RoleName myRole = myRoles.get(assignment.getChannelUser()
                                                    .getChannel()
                                                    .getChannelId());
            responseDtos.add(CalendarsResponseDto.fromAssignment(assignment, myRole));

            if (assignment.isEvaluation()) {
                responseDtos.add(CalendarsResponseDto.fromEvaluation(assignment, myRole));
            }
        });

        return responseDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarResponseDto> getCalendarsByDate(UUID userId, LocalDate dueDate) {
        LocalDateTime startDateTime = DateTimeUtil.getStartOfDay(dueDate);
        LocalDateTime endDateTime = DateTimeUtil.getEndOfDay(dueDate);

        List<Long> channelIds = channelUserRepository.findChannelIdsByUserId(userId);

        // 내 역할 캐싱
        Map<Long, RoleName> myRoles = channelUserRepository.findByUserUserId(userId)
                                                           .stream()
                                                           .collect(Collectors.toMap(
                                                                   cu -> cu.getChannel()
                                                                           .getChannelId(),
                                                                   cu -> cu.getRole()
                                                                           .getRoleName()));

        List<Course> courses =
                courseRepository.findCoursesInDateRange(channelIds, startDateTime, endDateTime);
        List<Assignment> assignments =
                assignmentRepository.findAssignmentsInDateRange(channelIds, startDateTime,
                                                                endDateTime);

        List<CalendarResponseDto> responseDtos = new ArrayList<>();

        courses.forEach(course -> {
            RoleName myRole = myRoles.get(course.getChannelUser()
                                                .getChannel()
                                                .getChannelId());
            responseDtos.add(CalendarResponseDto.fromCourse(course, myRole));
        });

        assignments.forEach(assignment -> {
            RoleName myRole = myRoles.get(assignment.getChannelUser()
                                                    .getChannel()
                                                    .getChannelId());
            responseDtos.add(CalendarResponseDto.fromAssignment(assignment, myRole));

            if (assignment.isEvaluation()) {
                responseDtos.add(CalendarResponseDto.fromEvaluation(assignment, myRole));
            }
        });

        return responseDtos;
    }
}
