package com.yuguanzhang.lumi.email.service;

import java.time.LocalDateTime;

public interface EmailVerificationCleanupService {
    void cleanupExpiredVerifications();
}
