package com.yuguanzhang.lumi.chat.service;

import com.yuguanzhang.lumi.chat.dto.ChatRequestDto;
import com.yuguanzhang.lumi.chat.dto.ChatRoomsResponseDto;
import com.yuguanzhang.lumi.chat.dto.ChatsResponseDto;
import com.yuguanzhang.lumi.chat.entity.Chat;
import com.yuguanzhang.lumi.chat.entity.Room;
import com.yuguanzhang.lumi.chat.entity.RoomUser;
import com.yuguanzhang.lumi.chat.repository.ChatRepository;
import com.yuguanzhang.lumi.chat.repository.RoomRepository;
import com.yuguanzhang.lumi.chat.repository.RoomUserRepository;
import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.user.entity.User;
import com.yuguanzhang.lumi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final RoomUserRepository roomUserRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;


    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomsResponseDto> getChatRooms(UUID userId) {

        // 사용자가 속한 방 목록 조회
        List<RoomUser> roomUsers = roomUserRepository.findByRoomUserId_UserId(userId);

        List<ChatRoomsResponseDto> result = new ArrayList<>();

        for (RoomUser ru : roomUsers) {
            Room room = ru.getRoom();

            // 채팅방의 상대
            RoomUser senderRoomUser =
                    roomUserRepository.findByRoomUserId_RoomIdAndRoomUserId_UserIdNot(
                                              room.getRoomId(), userId)
                                      .orElseThrow(() -> new GlobalException(
                                              ExceptionMessage.ROOM_USER_NOT_FOUND));

            // 상대방 정보
            User sender = senderRoomUser.getUser();

            ChatRoomsResponseDto dto = ChatRoomsResponseDto.fromEntity(room, sender, ru);

            result.add(dto);
        }
        return result;

    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatsResponseDto> getChats(UUID userId, Long roomId) {
        List<Chat> chatList = chatRepository.findByRoom_RoomId(roomId);

        chatList.forEach(chat -> {
            if (Boolean.FALSE.equals(chat.getIsRead()) && !chat.getUser()
                                                               .getUserId()
                                                               .equals(userId)) {
                chat.updateIsRead();
            }
        });

        // RoomUser hasUnread 초기화
        RoomUser roomUser =
                roomUserRepository.findByRoomUserId_RoomIdAndRoomUserId_UserId(roomId, userId)
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.ROOM_NOT_FOUND));
        roomUser.resetUnread();
        roomUserRepository.save(roomUser);

        return chatList.stream()
                       .map(ChatsResponseDto::fromEntity)
                       .toList();
    }

    @Override
    @Transactional
    public void deleteChat(UUID userId, Long roomId, Long chatId) {
        // 채팅 메세지 조회 (에러처리 필요)
        Chat chat = chatRepository.findByRoom_RoomIdAndChatId(roomId, chatId)
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.CHAT_NOT_FOUND));

        if (!chat.getUser()
                 .getUserId()
                 .equals(userId)) {
            throw new GlobalException(ExceptionMessage.UNAUTHORIZED_CHAT_DELETE);
        }

        chatRepository.delete(chat);

        // 삭제했을 때의 마지막 메세지 및 읽음 여부 업데이트 필요
        Chat lastChat = chatRepository.findTopByRoom_RoomIdOrderByCreatedAtDesc(roomId)
                                      .orElse(null);

        List<RoomUser> roomUsers = roomUserRepository.findByRoomUserId_RoomId(roomId);

        for (RoomUser ru : roomUsers) {
            if (lastChat != null) {
                // 마지막 메세지 갱싱
                ru.updateLastMessage(lastChat.getContent(), lastChat.getCreatedAt());

                boolean hasUnread = !lastChat.getUser()
                                             .getUserId()
                                             .equals(ru.getUser()
                                                       .getUserId()) && Boolean.FALSE.equals(
                        lastChat.getIsRead());


                if (hasUnread) {
                    ru.makeUnread();
                } else {
                    ru.resetUnread();
                }
            } else {
                // 채팅방에 메세지가 더 이상 없는 경우
                ru.updateLastMessage(null, null);
                ru.resetUnread();
            }

            roomUserRepository.save(ru);
        }
    }

    @Override
    @Transactional
    public RoomUser postChat(ChatRequestDto chatRequestDto, UUID userId, Long roomId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.USER_NOT_FOUND));

        Room room = roomRepository.findById(roomId)
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.ROOM_NOT_FOUND));

        Chat chat = chatRequestDto.toEntity(room, user);

        chatRepository.save(chat);

        log.info("chat: {}", chat);

        // 받는 사람 RoomUser의 lastMessage와 안읽은 메세지, 읽음 여부 업데이트
        RoomUser receiverRoomUser =
                roomUserRepository.findByRoomUserId_RoomIdAndRoomUserId_UserIdNot(roomId, userId)
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.ROOM_USER_NOT_FOUND));


        receiverRoomUser.updateLastMessage(chat.getContent(), chat.getCreatedAt());
        receiverRoomUser.makeUnread();

        // 보낸 사람 RoomUser의 lastMessage, 읽음 여부 업데이트
        RoomUser senderRoomUser =
                roomUserRepository.findByRoomUserId_RoomIdAndRoomUserId_UserId(roomId, userId)
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.ROOM_USER_NOT_FOUND));
        senderRoomUser.updateLastMessage(chat.getContent(), chat.getCreatedAt());
        senderRoomUser.resetUnread();

        return receiverRoomUser;
    }

}
