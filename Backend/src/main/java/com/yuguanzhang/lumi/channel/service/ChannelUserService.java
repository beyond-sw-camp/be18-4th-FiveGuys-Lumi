package com.yuguanzhang.lumi.channel.service;

import com.yuguanzhang.lumi.channel.dto.ChannelUserRequestDto;
import com.yuguanzhang.lumi.channel.dto.ChannelUserResponseDto;
import com.yuguanzhang.lumi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ChannelUserService {

    //유저 초대 성공시 채널 참가
    ChannelUserResponseDto joinChannel(String code, User user);

    //채널 내의 유저 리스트 조회
    Page<ChannelUserResponseDto> getChannelUsers(Long channelId, Pageable pageable);

    //채널 내의 유저 상세 조회
    ChannelUserResponseDto getChannelUser(Long channelId, UUID userId);

    //채널 내의 유저 정보 수정
    ChannelUserResponseDto updateChannelUserData(Long channelId, User user,
                                                 ChannelUserRequestDto channelUserRequestDto);

    //채널 내의 유저 탈퇴
    ChannelUserResponseDto leaveChannel(Long channelId, User user);


}
