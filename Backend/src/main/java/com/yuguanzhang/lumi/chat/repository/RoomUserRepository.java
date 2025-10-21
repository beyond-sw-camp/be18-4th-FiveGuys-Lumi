package com.yuguanzhang.lumi.chat.repository;

import com.yuguanzhang.lumi.chat.entity.RoomUser;
import com.yuguanzhang.lumi.chat.entity.RoomUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomUserRepository extends JpaRepository<RoomUser, RoomUserId> {
    // 내가 참여한 방
    List<RoomUser> findByRoomUserId_UserId(UUID userId);

    List<RoomUser> findByRoomUserId_RoomId(Long roomId);

    // 특정 Room에서의 상대방
    Optional<RoomUser> findByRoomUserId_RoomIdAndRoomUserId_UserIdNot(Long roomId, UUID userId);

    // 특정 Room에서의 나
    Optional<RoomUser> findByRoomUserId_RoomIdAndRoomUserId_UserId(Long roomId, UUID userId);
}
