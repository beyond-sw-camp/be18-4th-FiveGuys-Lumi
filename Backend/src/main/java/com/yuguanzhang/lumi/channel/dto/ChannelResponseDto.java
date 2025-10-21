package com.yuguanzhang.lumi.channel.dto;

import com.yuguanzhang.lumi.channel.entity.Channel;
import com.yuguanzhang.lumi.role.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class ChannelResponseDto {

    private final Long channelId;

    private final String name;

    private final String subject;

    private final UUID requestUserId;

    private final RoleName roleName;

    public static ChannelResponseDto fromEntity(Channel channel, UUID requestUserId) {
        return ChannelResponseDto.builder()
                                 .channelId(channel.getChannelId())
                                 .name(channel.getName())
                                 .subject(channel.getSubject())
                                 .requestUserId(requestUserId)
                                 .build();
    }

    public static ChannelResponseDto fromEntity(Channel channel) {
        return ChannelResponseDto.builder()
                                 .channelId(channel.getChannelId())
                                 .name(channel.getName())
                                 .subject(channel.getSubject())
                                 .build();
    }

    public static ChannelResponseDto fromEntity(Channel channel, RoleName roleName) {
        return ChannelResponseDto.builder()
                                 .channelId(channel.getChannelId())
                                 .name(channel.getName())
                                 .subject(channel.getSubject())
                                 .roleName(roleName)
                                 .build();
    }
}
