package com.yuguanzhang.lumi.email.entity;

import com.yuguanzhang.lumi.email.enums.VerificationStatus;
import com.yuguanzhang.lumi.user.entity.User;
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
@Table(name = "Email_Verifications")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id", updatable = false, nullable = false)
    private Long verificationId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "verification_code")
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VerificationStatus status;

    @Column(name = "expiration_at", nullable = false)
    private LocalDateTime expirationAt;

    @Column(name = "dateTime_at", nullable = false)
    private LocalDateTime dateTimeAt;

    public void markAsVerified() {
        this.status = VerificationStatus.VERIFIED;
    }

    public void markAsExpired() {
        this.status = VerificationStatus.EXPIRED;
        this.verificationCode = null;
    }

    public void markAsError() {
        this.status = VerificationStatus.ERROR;
    }

    // 이메일 재전송 시 UNREAD로 전환
    public void updateForResend(String newToken, LocalDateTime newExpirationTime) {
        this.verificationCode = newToken;
        this.expirationAt = newExpirationTime;
        this.status = VerificationStatus.UNREAD;
    }

    // User 엔티티와의 연관관계 설정 메서드
    public void associateUser(User user) {
        this.user = user;
    }


    public void setDateTimeAt(LocalDateTime dateTimeAt) {
        this.dateTimeAt = dateTimeAt;
    }

}
