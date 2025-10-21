package com.yuguanzhang.lumi.chat.repository;

import com.yuguanzhang.lumi.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
