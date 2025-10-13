package com.yuguanzhang.lumi.user.controller;

import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.common.jwt.dto.RefreshRequestDto;
import com.yuguanzhang.lumi.user.dto.logout.LogoutResponseDto;
import com.yuguanzhang.lumi.user.service.logout.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogoutController {
    private final LogoutService logoutService;

    // 로그아웃: 리프레시 토큰 삭제
    @PostMapping("/api/logout")
    public BaseResponseDto<LogoutResponseDto> logout(@RequestBody RefreshRequestDto request) {
        LogoutResponseDto response = logoutService.logout(request);
        return BaseResponseDto.of(HttpStatus.OK, response);
    }

}
