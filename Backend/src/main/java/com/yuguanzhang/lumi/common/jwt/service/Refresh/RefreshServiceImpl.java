package com.yuguanzhang.lumi.common.jwt.service.Refresh;

import com.yuguanzhang.lumi.user.dto.login.LoginResponseDto;
import com.yuguanzhang.lumi.common.jwt.dto.RefreshRequestDto;
import com.yuguanzhang.lumi.common.jwt.refresh.RefreshTokenStore;
import com.yuguanzhang.lumi.common.jwt.service.jwt.JwtService;
import com.yuguanzhang.lumi.user.entity.User;
import com.yuguanzhang.lumi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

    private final JwtService jwtService;
    private final RefreshTokenStore refreshTokenStore;
    private final UserRepository userRepository;

    @Override
    public LoginResponseDto refresh(RefreshRequestDto request) {
        String refreshToken = request.getRefreshToken();

        // refresh token 유효성 검사
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 RefreshToken");
        }

        // refresh token에서 email 추출
        String email = jwtService.getUsername(refreshToken);

        // 저장된 refresh token과 일치하는지 확인
        String saved = refreshTokenStore.find(email);
        if (saved == null || !saved.equals(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 불일치 또는 만료");
        }

        // 새 access token 발급
        String newAccess = jwtService.generateAccessToken(email);

        // DB에서 사용자 정보 조회 (name 가져오기)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 없음"));

        // LoginResponseDto 반환
        return new LoginResponseDto(user.getName(),   // name
                user.getEmail(),  // email
                newAccess, refreshToken);
    }
}
