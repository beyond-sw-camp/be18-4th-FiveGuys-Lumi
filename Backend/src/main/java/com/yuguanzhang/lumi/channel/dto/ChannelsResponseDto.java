package com.yuguanzhang.lumi.channel.dto;

import com.yuguanzhang.lumi.channel.entity.Channel;
import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.role.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
// 리스트 조회에서는 채널 생성자가 필요 없어서 dto 따로 만듦
public class ChannelsResponseDto {
    private final Long channelId;

    private final String name;

    private final String subject;

    private final RoleName roleName;


    public static ChannelsResponseDto fromEntity(ChannelUser cu) {
        return ChannelsResponseDto.builder()
                                  .channelId(cu.getChannel()
                                               .getChannelId())
                                  .name(cu.getChannel()
                                          .getName())
                                  .roleName(cu.getRole()
                                              .getRoleName())
                                  .subject(cu.getChannel()
                                             .getSubject())
                                  .build();

    }

}
