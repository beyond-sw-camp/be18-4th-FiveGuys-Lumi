package com.yuguanzhang.lumi.channel.repository;

import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelUserRepository extends JpaRepository<ChannelUser, Long> {

    Page<ChannelUser> findByChannel_ChannelId(Long channelId, Pageable pageable);

    //이 채널에 이 유저가 속해 있는지 찾는 메소드
    Optional<ChannelUser> findByChannel_ChannelIdAndUser_UserId(Long channelId, UUID userId);

    boolean existsByChannel_ChannelIdAndUser_UserId(Long channelId, UUID userId);

    // 특정 유저가 속한 모든 채널 조회
    //user 없음
    @Query("select cu.channel.channelId from ChannelUser cu where cu.user.userId = :userId")
    List<Long> findChannelIdsByUserId(UUID userId);

    List<ChannelUser> findByUserUserId(UUID userId);

    Page<ChannelUser> findByUser(User user, Pageable pageable);

}
