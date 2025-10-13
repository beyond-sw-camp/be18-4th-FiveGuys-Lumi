package com.yuguanzhang.lumi.user.service.logout;

import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.common.jwt.dto.RefreshRequestDto;
import com.yuguanzhang.lumi.common.jwt.refresh.RefreshTokenStore;
import com.yuguanzhang.lumi.common.jwt.service.jwt.JwtService;
import com.yuguanzhang.lumi.user.dto.logout.LogoutResponseDto;
import com.yuguanzhang.lumi.user.entity.User;
import com.yuguanzhang.lumi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final JwtService jwtService;
    private final RefreshTokenStore refreshTokenStore;
    private final UserRepository userRepository;

    @Override
    public LogoutResponseDto logout(RefreshRequestDto request) {
        String refreshToken = request.getRefreshToken();
        String name = null;
        String email = null;

        if (jwtService.validateRefreshToken(refreshToken)) {
            // refreshToken에서 email 추출
            email = jwtService.getUsername(refreshToken);

            // Redis에서 refreshToken 삭제
            refreshTokenStore.delete(email);

            // DB에서 사용자 조회 (name 가져오기)
            User user = userRepository.findByEmail(email)
                                      .orElseThrow(() -> new GlobalException(
                                              ExceptionMessage.ROOM_USER_NOT_FOUND));
            name = user.getName();
        }

        return new LogoutResponseDto(name, email);
    }

}
