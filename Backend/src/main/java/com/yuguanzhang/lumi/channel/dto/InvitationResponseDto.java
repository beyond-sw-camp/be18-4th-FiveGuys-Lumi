package com.yuguanzhang.lumi.channel.dto;

import com.yuguanzhang.lumi.channel.entity.Invitation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class InvitationResponseDto {

    private final Long invitationId;

    private final String invitationCode;

    private final Long channelId;

    private final Long roleId;

    private final LocalDateTime expiredAt;

    public static InvitationResponseDto fromEntity(Invitation invitation) {
        return InvitationResponseDto.builder()
                                    .invitationId(invitation.getInvitationId())
                                    .invitationCode(invitation.getInvitationCode())
                                    .channelId(invitation.getChannel()
                                                         .getChannelId())
                                    .roleId(invitation.getRole()
                                                      .getRoleId())
                                    .expiredAt(invitation.getExpiredAt())
                                    .build();
    }
}
