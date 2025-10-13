package com.yuguanzhang.lumi.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChatResponseDto {
    private final Long chatId;
}
