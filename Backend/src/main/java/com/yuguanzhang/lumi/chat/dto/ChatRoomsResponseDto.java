package com.yuguanzhang.lumi.chat.dto;

import com.yuguanzhang.lumi.chat.entity.Room;
import com.yuguanzhang.lumi.chat.entity.RoomUser;
import com.yuguanzhang.lumi.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class ChatRoomsResponseDto {
    private final Long roomId;
    private final String roomName;
    private final UUID opponentId;
    private final String opponentName;
    private final LocalDateTime lastMessageTime;
    private final String lastMessage;
    private final boolean hasUnread;

    public static ChatRoomsResponseDto fromEntity(Room room, User sender, RoomUser ru) {
        return ChatRoomsResponseDto.builder().roomId(room.getRoomId()).roomName(room.getName())
                                   .opponentId(sender.getUserId()).opponentName(sender.getName())
                                   .lastMessageTime(ru.getLastMessageTime())
                                   .lastMessage(ru.getLastMessageContent())
                                   .hasUnread(ru.isHasUnread()).build();

    }

}
