package com.clamos.signal.websocket.config;

import com.clamos.signal.websocket.handler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

    private final WebSocketDeviceHandler webSocketDeviceHandler;
    private final WebSocketManagerHandler webSocketManagerHandler;
    private final WebSocketWebHandler webSocketWebHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketDeviceHandler,"/device").setAllowedOrigins("*");
        registry.addHandler(webSocketManagerHandler,"/manager").setAllowedOrigins("*");
        registry.addHandler(webSocketWebHandler,"/web").setAllowedOrigins("*");
    }
}
