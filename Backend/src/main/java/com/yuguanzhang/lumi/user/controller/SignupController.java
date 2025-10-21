package com.yuguanzhang.lumi.user.controller;

import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.user.dto.sigup.SignupRequestDto;
import com.yuguanzhang.lumi.user.dto.sigup.SignupResponseDto;
import com.yuguanzhang.lumi.user.service.signup.SignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignUpService signUpService;

    @PostMapping("/api/sign-up")
    public BaseResponseDto<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto request) {
        SignupResponseDto responseDto = signUpService.processSignup(request);

        return BaseResponseDto.of(HttpStatus.OK, responseDto);
    }
}
