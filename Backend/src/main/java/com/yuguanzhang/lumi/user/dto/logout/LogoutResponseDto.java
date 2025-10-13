package com.yuguanzhang.lumi.user.dto.logout;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutResponseDto {
    private final String name;
    private final String email;
}
