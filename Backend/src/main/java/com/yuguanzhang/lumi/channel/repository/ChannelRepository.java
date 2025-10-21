package com.yuguanzhang.lumi.channel.repository;

import com.yuguanzhang.lumi.channel.entity.Channel;
import com.yuguanzhang.lumi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Page<Channel> findByChannelUsers_User(User user, Pageable pageable);

    Optional<Channel> findByChannelIdAndChannelUsers_User(Long channelId, User user);
}
