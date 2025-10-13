package com.yuguanzhang.lumi.user.service.login;

import com.yuguanzhang.lumi.common.exception.GlobalException;
import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import com.yuguanzhang.lumi.user.dto.login.LoginRequestDto;
import com.yuguanzhang.lumi.user.dto.login.LoginResponseDto;
import com.yuguanzhang.lumi.common.jwt.refresh.RefreshTokenStore;
import com.yuguanzhang.lumi.common.jwt.service.jwt.JwtService;
import com.yuguanzhang.lumi.user.entity.User;
import com.yuguanzhang.lumi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenStore refreshTokenStore;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshTtlMillis;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        if (request.getEmail() == null || request.getEmail()
                                                 .isBlank() || request.getPassword() == null || request.getPassword()
                                                                                                       .isBlank()) {
            throw new GlobalException(ExceptionMessage.ID_OR_PASSWORD_EMPTY);
        }

        // 클라이언트가 보낸 이메일과 비밀번호로 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // 인증 성공하면 UserDetails 객체를 가져옴 (사용자 정보 포함)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // DB에서 유저 조회 (name을 가져오기 위해)
        User user = userRepository.findByEmail(userDetails.getUsername())
                                  .orElseThrow(() -> new GlobalException(
                                          ExceptionMessage.ROOM_USER_NOT_FOUND));

        // AccessToken, RefreshToken 발급
        String accessToken = jwtService.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        // Redis에 Refresh 토큰 저장 (설정값 TTL 사용)
        refreshTokenStore.save(userDetails.getUsername(), refreshToken, refreshTtlMillis);

        // 생성한 JWT와 사용자 정보 반환
        return new LoginResponseDto(user.getName(), userDetails.getUsername(),   // email
                                    accessToken, refreshToken);
    }
}
