package com.yuguanzhang.lumi.chat.dto;


import com.yuguanzhang.lumi.chat.entity.Chat;
import com.yuguanzhang.lumi.chat.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class ChatsResponseDto {
    private final Long roomId;
    private final Long chatId;
    private final MessageType messageType;
    private final UUID senderId;
    private final String senderName;
    private final String message;
    private final LocalDateTime createdAt;
    private final List<String> files; // 추후 수정 필요

    public static ChatsResponseDto fromEntity(Chat chat) {
        return ChatsResponseDto.builder().roomId(chat.getRoom().getRoomId())
                .chatId(chat.getChatId()).messageType(chat.getMessageType())
                .senderId(chat.getUser().getUserId()).senderName(chat.getUser().getName())
                .message(chat.getContent()).createdAt(chat.getCreatedAt()).build();
    }
}
