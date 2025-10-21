package com.yuguanzhang.lumi.course.service;

import com.yuguanzhang.lumi.course.dto.CourseRequestDto;
import com.yuguanzhang.lumi.course.dto.CourseResponseDto;
import com.yuguanzhang.lumi.course.dto.CourseStatusUpdateRequestDto;
import com.yuguanzhang.lumi.course.dto.CoursesResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CourseService {
    List<CoursesResponseDto> getCourses(UUID userId, LocalDate startDate, LocalDate endDate);

    List<CourseResponseDto> getCoursesByDate(UUID userId, LocalDate dueDate);

    CourseResponseDto getCourse(UUID userId, Long courseId);

    CourseResponseDto createCourse(UUID userId, Long channelId, CourseRequestDto courseRequestDto);

    CourseResponseDto updateCourse(UUID userId, Long channelId, CourseRequestDto courseRequestDto,
                                   Long courseId);

    CourseResponseDto updateCourseStatus(UUID userId, Long channelId,
                                         CourseStatusUpdateRequestDto courseStatusUpdateRequestDto,
                                         Long courseId);

    void deleteCourse(UUID userId, Long channelId, Long courseId);

}
