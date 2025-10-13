package com.yuguanzhang.lumi.user.controller;

import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.user.dto.login.LoginRequestDto;
import com.yuguanzhang.lumi.user.dto.login.LoginResponseDto;
import com.yuguanzhang.lumi.user.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/api/login")
    public BaseResponseDto<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = loginService.login(request);

        return BaseResponseDto.of(HttpStatus.OK, response);
    }
}
