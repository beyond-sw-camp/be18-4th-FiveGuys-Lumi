package com.yuguanzhang.lumi.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "provider")
    private String provider;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_privacy_agreement", nullable = false)
    private Boolean isPrivacyAgreement;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Column(name = "is_deleted", nullable = false)
    private String isDeleted = "N";

    public void markAsVerified() {
        this.isVerified = true;
    }

    public boolean getIsVerified() {
        return this.isVerified;
    }

    public void markAsDeleted() {
        this.isDeleted = "Y";
        this.deletedAt = LocalDateTime.now();
    }

    public boolean getIsDeleted() {
        return "Y".equals(this.isDeleted);
    }


}
