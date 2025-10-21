package com.yuguanzhang.lumi.common.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private final String email;
    private final String accessToken;
    private final String refreshToken;
}
