package com.yuguanzhang.lumi.email.service;

public interface EmailVerificationService {

    void sendVerificationEmail(String email);

    String verifyEmail(String token);

    boolean isEmailVerified(String email);
}
