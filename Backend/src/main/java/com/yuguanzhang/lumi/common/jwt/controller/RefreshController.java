package com.yuguanzhang.lumi.common.jwt.controller;

import com.yuguanzhang.lumi.user.dto.login.LoginResponseDto;
import com.yuguanzhang.lumi.common.jwt.dto.RefreshRequestDto;
import com.yuguanzhang.lumi.common.jwt.refresh.RefreshTokenStore;
import com.yuguanzhang.lumi.common.jwt.service.jwt.JwtService;
import com.yuguanzhang.lumi.common.jwt.service.Refresh.RefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final JwtService jwtService;
    private final RefreshTokenStore refreshTokenStore;
    private final RefreshService refreshService;


    // 리프레시 토큰으로 새로운 Access Token 발급
    @PostMapping("/api/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshRequestDto request) {
        LoginResponseDto response = refreshService.refresh(request);
        return ResponseEntity.ok(response);
    }
}
