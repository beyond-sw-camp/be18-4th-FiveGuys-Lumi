package com.yuguanzhang.lumi.common.jwt;

import com.yuguanzhang.lumi.common.jwt.service.jwt.JwtService;
import com.yuguanzhang.lumi.user.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/api/email/")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = resolveToken(
                    request); // HTTP 헤더 "Authorization" 에서 "Bearer <JWT토큰>" 형태로 들어온 값을 꺼냄. 없다면 null 리턴.
            if (token != null && jwtService.validateAccessToken(
                    token)) { // 토큰이 만료 안 됐는지, 서명 위조 안 됐는지 확인.
                String username =
                        jwtService.getUsername(token); // 토큰 안에 들어있는 username(보통 sub claim)을 가져옴.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities()); // 권한(ROLE_USER, ROLE_ADMIN 등)을 같이 넣어줌
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                        request)); // 요청 정보(IP, 세션 등)도 함께 저장.

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

