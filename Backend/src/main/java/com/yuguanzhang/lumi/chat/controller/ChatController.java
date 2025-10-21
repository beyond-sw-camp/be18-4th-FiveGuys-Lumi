package com.yuguanzhang.lumi.chat.controller;

import com.yuguanzhang.lumi.chat.dto.ChatRequestDto;
import com.yuguanzhang.lumi.chat.dto.ChatRoomsResponseDto;
import com.yuguanzhang.lumi.chat.dto.ChatsResponseDto;
import com.yuguanzhang.lumi.chat.dto.UnreadUpdateResponseDto;
import com.yuguanzhang.lumi.chat.entity.RoomUser;
import com.yuguanzhang.lumi.chat.service.ChatService;
import com.yuguanzhang.lumi.common.dto.BaseResponseDto;
import com.yuguanzhang.lumi.user.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final SimpMessageSendingOperations template;

    @GetMapping
    public ResponseEntity<BaseResponseDto<ChatRoomsResponseDto>> getChatRooms(
            @AuthenticationPrincipal UserDetailsDto user) {
        List<ChatRoomsResponseDto> rooms = chatService.getChatRooms(user.getUser().getUserId());

        BaseResponseDto<ChatRoomsResponseDto> response = BaseResponseDto.of(HttpStatus.OK, rooms);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{room_id}")
    public ResponseEntity<BaseResponseDto<ChatsResponseDto>> getChats(
            @AuthenticationPrincipal UserDetailsDto user, @PathVariable("room_id") Long roomId) {
        List<ChatsResponseDto> chats = chatService.getChats(user.getUser().getUserId(), roomId);

        BaseResponseDto<ChatsResponseDto> response = BaseResponseDto.of(HttpStatus.OK, chats);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{room_id}/chats/{chat_id}")
    public ResponseEntity<BaseResponseDto<Void>> deleteChat(
            @AuthenticationPrincipal UserDetailsDto user, @PathVariable("room_id") Long roomId,
            @PathVariable("chat_id") Long chatId) {

        chatService.deleteChat(user.getUser().getUserId(), roomId, chatId);

        BaseResponseDto<Void> response = BaseResponseDto.of(HttpStatus.OK, null);

        return ResponseEntity.ok(response);

    }

    @MessageMapping("/{room_id}/chats")
    public void sendMessage(@AuthenticationPrincipal UserDetailsDto user,
                            @Payload ChatRequestDto chatRequestDto,
                            @DestinationVariable("room_id") Long roomId) {

        log.info("user: {}", user);

        RoomUser receiverRoomUser =
                chatService.postChat(chatRequestDto, user.getUser().getUserId(), roomId);

        String receiverId = receiverRoomUser.getRoomUserId().getUserId().toString();

        // 메세지를 서버로 부터 받음
        template.convertAndSend("/sub/chatrooms/" + roomId, chatRequestDto);

        // 채팅 목록 업데이트 이벤트
        template.convertAndSendToUser(receiverId, "/queue/unread",
                                      new UnreadUpdateResponseDto(roomId,
                                                                  receiverRoomUser.isHasUnread(),
                                                                  chatRequestDto.getMessage(),
                                                                  LocalDateTime.now(),
                                                                  user.getUser().getUserId()));
    }
}
