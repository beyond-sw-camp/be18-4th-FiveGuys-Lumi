package com.yuguanzhang.lumi.channel.service;

import com.yuguanzhang.lumi.channel.dto.InvitationRequestDto;
import com.yuguanzhang.lumi.channel.dto.InvitationResponseDto;
import com.yuguanzhang.lumi.user.entity.User;

public interface InvitationService {

    InvitationResponseDto createInvitation(Long channelId, User user,
                                           InvitationRequestDto invitationRequestDto);
}
