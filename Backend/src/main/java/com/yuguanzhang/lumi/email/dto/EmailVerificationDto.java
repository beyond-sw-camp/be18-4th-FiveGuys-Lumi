package com.yuguanzhang.lumi.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EmailVerificationDto {
    private final String email;
}
