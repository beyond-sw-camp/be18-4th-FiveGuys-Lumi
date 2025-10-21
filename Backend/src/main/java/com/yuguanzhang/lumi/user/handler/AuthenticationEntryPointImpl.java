package com.yuguanzhang.lumi.user.handler;

import com.yuguanzhang.lumi.common.exception.dto.ApiErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // Jackson 라이브러리에서 제공하는 ObjectMapper를 사용하여 자바 객체를 JSON 문자열로 변환 후 응답
        ObjectMapper objectMapper = new ObjectMapper();
        ApiErrorResponseDto apiErrorResponseDto =
                new ApiErrorResponseDto(HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.name(), HttpStatus.UNAUTHORIZED.getReasonPhrase());

        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponseDto));
    }
}
