package com.yuguanzhang.lumi.channel.service;

import com.yuguanzhang.lumi.channel.dto.ChannelsResponseDto;
import com.yuguanzhang.lumi.channel.dto.ChannelRequestDto;
import com.yuguanzhang.lumi.channel.dto.ChannelResponseDto;
import com.yuguanzhang.lumi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChannelService {

    //채널 생성
    ChannelResponseDto createChannel(User user, ChannelRequestDto channelRequestDto);

    //채널 리스트 조회
    Page<ChannelsResponseDto> getChannels(User user, Pageable pageable);

    //채널 상세 조회
    ChannelResponseDto getChannel(User user, Long channelId);

    //채널 수정
    ChannelResponseDto updateChannel(Long channelId, User user,
                                     ChannelRequestDto channelRequestDto);

    //채널 삭제
    ChannelResponseDto deleteChannel(Long channelId, User user);

}
