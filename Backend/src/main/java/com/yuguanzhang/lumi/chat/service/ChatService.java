package com.yuguanzhang.lumi.chat.service;

import com.yuguanzhang.lumi.chat.dto.ChatRequestDto;
import com.yuguanzhang.lumi.chat.dto.ChatRoomsResponseDto;
import com.yuguanzhang.lumi.chat.dto.ChatsResponseDto;
import com.yuguanzhang.lumi.chat.entity.RoomUser;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    List<ChatRoomsResponseDto> getChatRooms(UUID userId);

    List<ChatsResponseDto> getChats(UUID userId, Long roomId);

    void deleteChat(UUID userId, Long roomId, Long chatId);

    RoomUser postChat(ChatRequestDto chatRequestDto, UUID userId, Long roomId);
}
