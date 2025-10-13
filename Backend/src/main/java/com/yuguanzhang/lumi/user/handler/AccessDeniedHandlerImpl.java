package com.yuguanzhang.lumi.user.handler;

import com.yuguanzhang.lumi.common.exception.dto.ApiErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // Jackson 라이브러리에서 제공하는 ObjectMapper를 사용하여 자바 객체를 JSON 문자열로 변환 후 응답
        ObjectMapper objectMapper = new ObjectMapper();
        ApiErrorResponseDto apiErrorResponseDto =
                new ApiErrorResponseDto(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.name(),
                        HttpStatus.FORBIDDEN.getReasonPhrase());

        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponseDto));
    }
}
