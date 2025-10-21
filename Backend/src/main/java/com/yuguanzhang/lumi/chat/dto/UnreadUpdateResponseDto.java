package com.yuguanzhang.lumi.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class UnreadUpdateResponseDto {
    private final Long roomId;
    private final boolean hasUnread;
    private final String lastMessage;
    private final LocalDateTime lastMessageTime;
    private final UUID opponentId;
}
