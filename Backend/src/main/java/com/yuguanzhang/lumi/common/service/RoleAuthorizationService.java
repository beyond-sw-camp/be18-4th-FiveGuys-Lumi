package com.yuguanzhang.lumi.common.service;

import com.yuguanzhang.lumi.channel.entity.ChannelUser;
import com.yuguanzhang.lumi.channel.repository.ChannelUserRepository;
import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.role.entity.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleAuthorizationService {

    private final ChannelUserRepository channelUserRepository;

    //튜터 검증
    public void checkTutor(Long channelId, UUID userId) {

        ChannelUser channelUser =
                channelUserRepository.findByChannel_ChannelIdAndUser_UserId(channelId, userId)
                                     .orElseThrow(() -> new GlobalException(
                                             ExceptionMessage.CHANNEL_USER_NOT_FOUND));

        if (channelUser.getRole()
                       .getRoleName() != RoleName.TUTOR) {
            throw new GlobalException(ExceptionMessage.TUTOR_ROLE_REQUIRED);
        }
    }

    public void checkNotTutor(Long channelId, UUID userId) {

        ChannelUser channelUser =
                channelUserRepository.findByChannel_ChannelIdAndUser_UserId(channelId, userId)
                                     .orElseThrow(() -> new GlobalException(
                                             ExceptionMessage.CHANNEL_USER_NOT_FOUND));

        if (channelUser.getRole()
                       .getRoleName() == RoleName.TUTOR) {
            throw new GlobalException(ExceptionMessage.TUTOR_NOT_AVAILABLE);
        }
    }

    public void checkStudent(Long channelId, UUID userId) {

        ChannelUser channelUser =
                channelUserRepository.findByChannel_ChannelIdAndUser_UserId(channelId, userId)
                                     .orElseThrow(() -> new GlobalException(
                                             ExceptionMessage.CHANNEL_USER_NOT_FOUND));
        if (channelUser.getRole()
                       .getRoleName() != RoleName.STUDENT) {
            throw new GlobalException(ExceptionMessage.STUDENT_ROLE_REQUIRED);
        }
    }

}
