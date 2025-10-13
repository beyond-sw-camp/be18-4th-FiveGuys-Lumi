package com.yuguanzhang.lumi.grade.Entity;

import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "Grades")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long gradeId;

    //성적의 제목 ex.2학년 2학기 중간고사
    @Column(name = "title", nullable = false)
    private String title;

    //비교군을 위한 카테고리
    @Column(name = "category", nullable = false)
    private String category;


    @Column(name = "grades", nullable = false)
    private int grades;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_user_id", nullable = false)
    private ChannelUser channelUser;

    public void updateGrade(String title, String category, int grades, LocalDate date) {
        this.title = title;
        this.category = category;
        this.grades = grades;
        this.date = date;
    }
}
