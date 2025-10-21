package com.yuguanzhang.lumi.assignment.entity;

import com.yuguanzhang.lumi.common.entity.BaseTimeEntity;
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

@Entity
@Getter
@Table(name = "Evaluations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "feedback")
    private String feedback;

    // 제출과 1:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", unique = true)
    private Submission submission;

    public void updateGrade(Integer grade) {
        this.grade = grade;
    }

    public void updateFeedback(String feedback) {
        this.feedback = feedback;
    }
}
