package com.yuguanzhang.lumi.chat.dto;

import com.yuguanzhang.lumi.chat.entity.Chat;
import com.yuguanzhang.lumi.chat.entity.Room;
import com.yuguanzhang.lumi.chat.enums.MessageType;
import com.yuguanzhang.lumi.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChatRequestDto {
    private MessageType messageType;

    private String message;

    private Long receiverId;

    public Chat toEntity(Room room, User user) {
        return Chat.builder()
                   .room(room)
                   .user(user)
                   .content(this.message)
                   .messageType(this.messageType)
                   .isRead(false)
                   .build();

    }

}
