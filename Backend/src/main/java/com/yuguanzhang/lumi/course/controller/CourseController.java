package com.yuguanzhang.lumi.course.controller;


import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.course.dto.CourseRequestDto;
import com.yuguanzhang.lumi.course.dto.CourseResponseDto;
import com.yuguanzhang.lumi.course.dto.CourseStatusUpdateRequestDto;
import com.yuguanzhang.lumi.course.dto.CoursesResponseDto;
import com.yuguanzhang.lumi.course.service.CourseService;
import com.yuguanzhang.lumi.user.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Slf4j
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<BaseResponseDto<CoursesResponseDto>> getCourses(
            @AuthenticationPrincipal UserDetailsDto user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {
        List<CoursesResponseDto> courses = courseService.getCourses(user.getUser()
                                                                        .getUserId(), startDate,
                                                                    endDate);

        BaseResponseDto<CoursesResponseDto> response = BaseResponseDto.of(HttpStatus.OK, courses);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/date/{date}")
    public ResponseEntity<BaseResponseDto<CourseResponseDto>> getCoursesByDate(
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dueDate) {
        List<CourseResponseDto> courses = courseService.getCoursesByDate(user.getUser()
                                                                             .getUserId(), dueDate);

        BaseResponseDto<CourseResponseDto> response = BaseResponseDto.of(HttpStatus.OK, courses);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/{course_id}")
    public ResponseEntity<BaseResponseDto<CourseResponseDto>> getCourse(
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("course_id") Long courseId) {
        CourseResponseDto courses = courseService.getCourse(user.getUser()
                                                                .getUserId(), courseId);

        BaseResponseDto<CourseResponseDto> response = BaseResponseDto.of(HttpStatus.OK, courses);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{channel_id}/courses")
    public ResponseEntity<BaseResponseDto<CourseResponseDto>> createCourse(
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("channel_id") Long channelId,
            @RequestBody CourseRequestDto courseRequestDto) {
        CourseResponseDto course = courseService.createCourse(user.getUser()
                                                                  .getUserId(), channelId,
                                                              courseRequestDto);

        BaseResponseDto<CourseResponseDto> response =
                BaseResponseDto.of(HttpStatus.CREATED, course);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    @PatchMapping("/{channel_id}/courses/{course_id}")
    public ResponseEntity<BaseResponseDto<CourseResponseDto>> updateCourse(
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("channel_id") Long channelId, @PathVariable("course_id") Long courseId,
            @RequestBody CourseRequestDto courseRequestDto) {
        CourseResponseDto course = courseService.updateCourse(user.getUser()
                                                                  .getUserId(), channelId,
                                                              courseRequestDto, courseId);

        BaseResponseDto<CourseResponseDto> response = BaseResponseDto.of(HttpStatus.OK, course);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{channel_id}/courses/{course_id}/status")
    public ResponseEntity<BaseResponseDto<CourseResponseDto>> updateCourse(
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("channel_id") Long channelId, @PathVariable("course_id") Long courseId,
            @RequestBody CourseStatusUpdateRequestDto courseStatusUpdateRequestDto) {
        CourseResponseDto course = courseService.updateCourseStatus(user.getUser()
                                                                        .getUserId(), channelId,
                                                                    courseStatusUpdateRequestDto,
                                                                    courseId);

        BaseResponseDto<CourseResponseDto> response = BaseResponseDto.of(HttpStatus.OK, course);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{channel_id}/courses/{course_id}")
    public ResponseEntity<BaseResponseDto<Void>> deleteCourse(
            @AuthenticationPrincipal UserDetailsDto user,
            @PathVariable("channel_id") Long channelId, @PathVariable("course_id") Long courseId) {
        courseService.deleteCourse(user.getUser()
                                       .getUserId(), channelId, courseId);

        BaseResponseDto<Void> response = BaseResponseDto.of(HttpStatus.OK, null);

        return ResponseEntity.ok(response);
    }


}
