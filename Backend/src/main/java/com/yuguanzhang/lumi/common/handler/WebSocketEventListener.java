package com.yuguanzhang.lumi.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userInfo = auth != null ? auth.getName() : "익명 사용자";
        log.info("WebSocket 연결됨: 세션 ID = {}, 사용자 = {}", event.getUser(), userInfo);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("WebSocket 연결 해제됨: 세션 ID = {}", event.getUser());
    }
}
