package com.yuguanzhang.lumi.user.service.deleted;

import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.common.jwt.refresh.RefreshTokenStore;
import com.yuguanzhang.lumi.user.dto.deleted.DeletedRequestDto;
import com.yuguanzhang.lumi.user.dto.deleted.DeletedResponseDto;
import com.yuguanzhang.lumi.user.entity.User;
import com.yuguanzhang.lumi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeletedServiceImpl implements DeletedService {

    private final UserRepository userRepository;
    private final RefreshTokenStore refreshTokenStore;

    @Override
    @Transactional
    public DeletedResponseDto deleted(DeletedRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.ROOM_USER_NOT_FOUND));

        user.markAsDeleted();

        refreshTokenStore.delete(user.getEmail());
        return new DeletedResponseDto(user.getEmail());
    }
}
