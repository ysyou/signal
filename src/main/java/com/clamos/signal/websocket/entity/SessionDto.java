package com.clamos.signal.websocket.entity;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Data
public class SessionDto {
    private WebSocketSession session;
    private String id;
}
