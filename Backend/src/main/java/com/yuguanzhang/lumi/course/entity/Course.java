package com.yuguanzhang.lumi.course.entity;

import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.common.entity.BaseTimeEntity;
import com.yuguanzhang.lumi.course.dto.CourseRequestDto;
import com.yuguanzhang.lumi.course.enums.StatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Courses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_user_id")
    private ChannelUser channelUser;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType statusType = StatusType.SCHEDULED;

    public void updateCourse(CourseRequestDto course) {
        this.startDate = course.getStartDate();
        this.endDate = course.getEndDate();
        this.location = course.getLocation();
        this.statusType = course.getStatusType();
    }

    public void updateCourseStatus(StatusType statusType) {
        this.statusType = statusType;
    }


}
