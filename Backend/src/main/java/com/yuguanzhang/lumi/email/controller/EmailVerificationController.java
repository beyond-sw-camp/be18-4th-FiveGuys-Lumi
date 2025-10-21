package com.yuguanzhang.lumi.email.controller;

import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.email.dto.EmailVerificationDto;
import com.yuguanzhang.lumi.email.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    // 이메일 인증 메일 발송
    @PostMapping("/api/email/send")
    public BaseResponseDto<Void> sendEmailVerification(
            @RequestBody EmailVerificationDto emailVerificationDto) {
        emailVerificationService.sendVerificationEmail(emailVerificationDto.getEmail());
        return BaseResponseDto.of(HttpStatus.OK, null);
    }

    // 이메일 토큰 검증
    // BaseResponseDto를 뺀 이유: 이메일 인증 완료를 보낼 시 감싸져 있으면 공통 설정까지 보내져서 뺌
    @GetMapping("/api/email/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        String message = emailVerificationService.verifyEmail(token);
        return ResponseEntity.ok()
                             .header("Content-Type", "text/plain; charset=UTF-8")
                             .body(message);
    }

}
