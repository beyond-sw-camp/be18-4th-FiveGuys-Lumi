package com.yuguanzhang.lumi.common.config;

import com.yuguanzhang.lumi.common.jwt.JwtAuthenticationFilter;
import com.yuguanzhang.lumi.user.handler.AccessDeniedHandlerImpl;
import com.yuguanzhang.lumi.user.handler.AuthenticationEntryPointImpl;
import com.yuguanzhang.lumi.user.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    // PasswordEncoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     AuthenticationManager를 빈으로 노출하는 최신 방식입니다. Spring Security가 UserDetailsService와
     PasswordEncoder를 자동으로 감지하므로 DaoAuthenticationProvider를 직접 구성할 필요가 없습니다. 이 방식은
     DaoAuthenticationProvider 관련 더 이상 사용되지 않는 경고를 제거합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // SecurityFilterChain Bean
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .cors(cors -> {
                })             // CORS 허용
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(
                                new AuthenticationEntryPointImpl())
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())).authorizeHttpRequests(
                        auth -> auth.requestMatchers("/", "/api/login", "/api/sign-up", "/api/refresh",
                                        "/api/logout", "/api/public/**", "/api/email/**").permitAll().requestMatchers("/api/chatrooms/**").authenticated().anyRequest()
                                .authenticated()).addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
