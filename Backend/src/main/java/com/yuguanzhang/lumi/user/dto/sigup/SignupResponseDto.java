package com.yuguanzhang.lumi.user.dto.sigup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupResponseDto {
    private final String message;
    private final String email;
    private final String name;
}
