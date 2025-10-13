package com.yuguanzhang.lumi.email.repository;

import com.yuguanzhang.lumi.email.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {

    Optional<EmailVerification> findByEmail(String email);
    // 특정 유저의 특정 상태 인증이 존재하는지 여부 / 나중에 고도화 할 때 사용할려고 넣은거임
    // boolean existsByUserAndStatus(User user, VerificationStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE EmailVerification ev SET ev.status = 'EXPIRED', ev.verificationCode = NULL WHERE ev.expirationAt < :expirationTime")
    void updateVerifications(@Param("expirationTime") LocalDateTime expirationTime);
}   // @Param 이거 넣은 이유가 바인딩하는게 더 좋다고 하길래 넣어봄
