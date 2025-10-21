package com.yuguanzhang.lumi.chat.repository;

import com.yuguanzhang.lumi.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByRoom_RoomId(Long roomId);

    Optional<Chat> findByRoom_RoomIdAndChatId(Long roomId, Long chatId);

    Optional<Chat> findTopByRoom_RoomIdOrderByCreatedAtDesc(Long roomId);
}
