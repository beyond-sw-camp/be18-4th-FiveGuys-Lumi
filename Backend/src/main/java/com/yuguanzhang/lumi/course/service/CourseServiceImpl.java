package com.yuguanzhang.lumi.course.service;

import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.channel.repository.ChannelUserRepository;
import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.common.service.RoleAuthorizationService;
import com.yuguanzhang.lumi.common.util.DateRangeUtil;
import com.yuguanzhang.lumi.common.util.DateTimeUtil;
import com.yuguanzhang.lumi.common.util.LocalDateRange;
import com.yuguanzhang.lumi.course.dto.CourseRequestDto;
import com.yuguanzhang.lumi.course.dto.CourseResponseDto;
import com.yuguanzhang.lumi.course.dto.CourseStatusUpdateRequestDto;
import com.yuguanzhang.lumi.course.dto.CoursesResponseDto;
import com.yuguanzhang.lumi.course.entity.Course;
import com.yuguanzhang.lumi.course.respository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ChannelUserRepository channelUserRepository;
    private final RoleAuthorizationService roleAuthorizationService;

    @Override
    @Transactional(readOnly = true)
    public List<CoursesResponseDto> getCourses(UUID userId, LocalDate startDate,
                                               LocalDate endDate) {
        LocalDateRange range = DateRangeUtil.getMonthlyRange(startDate, endDate);
        LocalDateTime startDateTime = range.getStartDateTime();
        LocalDateTime endDateTime = range.getEndDateTime();

        List<Long> channelIds = channelUserRepository.findChannelIdsByUserId(userId);

        List<Course> courses =
                courseRepository.findCoursesInDateRange(channelIds, startDateTime, endDateTime);

        log.info("courses :{}", courses.toString());



        return courses.stream()
                      .map(CoursesResponseDto::fromEntity)
                      .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponseDto> getCoursesByDate(UUID userId, LocalDate dueDate) {
        LocalDateTime startDateTime = DateTimeUtil.getStartOfDay(dueDate);
        LocalDateTime endDateTime = DateTimeUtil.getEndOfDay(dueDate);


        List<Long> channelIds = channelUserRepository.findChannelIdsByUserId(userId);

        List<Course> courses =
                courseRepository.findCoursesInDateRange(channelIds, startDateTime, endDateTime);


        return courses.stream()
                      .map(CourseResponseDto::fromEntity)
                      .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponseDto getCourse(UUID userId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new GlobalException(
                                                ExceptionMessage.COURSE_NOT_FOUND));

        return CourseResponseDto.fromEntity(course);
    }

    @Override
    @Transactional
    public CourseResponseDto createCourse(UUID userId, Long channelId,
                                          CourseRequestDto courseRequestDto) {
        ChannelUser channelUser =
                channelUserRepository.findByChannel_ChannelIdAndUser_UserId(channelId, userId)
                                     .orElseThrow(() -> new GlobalException(
                                             ExceptionMessage.CHANNEL_USER_NOT_FOUND));

        roleAuthorizationService.checkTutor(channelId, userId);

        Course course = courseRequestDto.toEntity(channelUser);

        courseRepository.save(course);

        return CourseResponseDto.fromEntity(course);
    }

    @Override
    @Transactional
    public CourseResponseDto updateCourse(UUID userId, Long channelId,
                                          CourseRequestDto courseRequestDto, Long courseId) {
        roleAuthorizationService.checkTutor(channelId, userId);

        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new GlobalException(
                                                ExceptionMessage.COURSE_NOT_FOUND));
        course.updateCourse(courseRequestDto);

        return CourseResponseDto.fromEntity(course);
    }

    @Override
    @Transactional
    public CourseResponseDto updateCourseStatus(UUID userId, Long channelId,
                                                CourseStatusUpdateRequestDto courseStatusUpdateRequestDto,
                                                Long courseId) {
        roleAuthorizationService.checkTutor(channelId, userId);

        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new GlobalException(
                                                ExceptionMessage.COURSE_NOT_FOUND));

        course.updateCourseStatus(courseStatusUpdateRequestDto.getStatusType());

        return CourseResponseDto.fromEntity(course);
    }

    @Override
    @Transactional
    public void deleteCourse(UUID userId, Long channelId, Long courseId) {
        roleAuthorizationService.checkTutor(channelId, userId);

        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new GlobalException(
                                                ExceptionMessage.COURSE_NOT_FOUND));

        courseRepository.delete(course);
    }
}
