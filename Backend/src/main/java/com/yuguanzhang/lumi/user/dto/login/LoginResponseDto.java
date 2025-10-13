package com.yuguanzhang.lumi.user.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private final String name;
    private final String email;
    private final String accessToken;
    private final String refreshToken;
}
