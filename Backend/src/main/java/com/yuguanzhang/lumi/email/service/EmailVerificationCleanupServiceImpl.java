package com.yuguanzhang.lumi.email.service;

import com.yuguanzhang.lumi.email.repository.EmailVerificationRepository;
import com.yuguanzhang.lumi.email.service.EmailVerificationCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationCleanupServiceImpl implements EmailVerificationCleanupService {

    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public void cleanupExpiredVerifications() {
        LocalDateTime now = LocalDateTime.now();
        emailVerificationRepository.updateVerifications(now);
        log.info("만료된 이메일 인증 상태 변경 완료. 시간: {}", now);
    }
}
