package com.yuguanzhang.lumi.assignment.entity;

import com.yuguanzhang.lumi.assignment.dto.AssignmentRequestDto;
import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.common.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Assignments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "deadline_at", nullable = false)
    private LocalDateTime deadlineAt;  // 제출 마감일 (날짜만)

    @Column(name = "is_evaluation", nullable = false)
    private boolean isEvaluation;      // 평가 여부

    @Column(name = "is_submission", nullable = false)
    private boolean isSubmission;      // 제출 가능 여부

    @Column(name = "evaluation_deadline_at")
    private LocalDateTime evaluationDeadlineAt;  // 평가 마감일 (날짜만)

    // 선생님 (과제 생성자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_user_id", nullable = false)
    private ChannelUser channelUser;



    public void updateAssignment(AssignmentRequestDto assignmentRequestDto) {
        this.title = assignmentRequestDto.getTitle();
        this.content = assignmentRequestDto.getContent();
        this.deadlineAt = assignmentRequestDto.getDeadlineAt();
        this.isEvaluation = assignmentRequestDto.isEvaluation();
        this.evaluationDeadlineAt = assignmentRequestDto.getDeadlineAt()
                                                        .plusDays(7);
    }

    public void updateIsSubmission(boolean isSubmission) {
        this.isSubmission = isSubmission;
    }
}
