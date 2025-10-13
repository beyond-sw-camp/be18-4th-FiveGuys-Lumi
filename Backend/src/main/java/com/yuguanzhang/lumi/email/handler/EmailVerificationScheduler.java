package com.yuguanzhang.lumi.email.handler;

import com.yuguanzhang.lumi.email.service.EmailVerificationCleanupServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationScheduler {

    private final EmailVerificationCleanupServiceImpl cleanupService;

    @Scheduled(fixedDelay = 600000)
    public void runCleanup() {
        log.info("만료된 이메일 상태 전환 시작");
        cleanupService.cleanupExpiredVerifications();
    }
}
