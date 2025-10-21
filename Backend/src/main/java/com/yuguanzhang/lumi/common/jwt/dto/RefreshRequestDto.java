package com.yuguanzhang.lumi.common.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor  // 모든 final 필드를 초기화하는 생성자
public class RefreshRequestDto {
    private final String refreshToken;
}
