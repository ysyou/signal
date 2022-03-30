package com.clamos.signal.websocket.handler;

import com.clamos.signal.websocket.entity.CommandDto;
import com.clamos.signal.websocket.entity.ResponseDto;
import com.clamos.signal.websocket.entity.SessionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class WebSocketManagerHandler extends TextWebSocketHandler {
    @Autowired
    ObjectMapper objectMapper;

    private static List<SessionDto> sessionList = new CopyOnWriteArrayList<>();

    // connection opened
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setSession(session);
        log.info("session connected : " + session);
    }

    // connection closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("session disconnected : " + session);
        // 세션 종료시 sessionId 삭제
        for(int i=0; i<sessionList.size(); i++){
            if(sessionList.get(i).getSession().equals(session)){
                sessionList.remove(i);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Device payload : {}",payload);
        CommandDto commandDto = objectMapper.readValue(payload, CommandDto.class);
        ResponseDto res = null;

        if(commandDto.getCmd().equals("eventStart")){

        }else if(commandDto.getCmd().equals("eventStop")){

        } else if(commandDto.getCmd().equals("keepAlive")){ // 핑퐁이되면 삭제
            res = new ResponseDto();
            res.setCmd(commandDto.getCmd());
            res.setTag(commandDto.getTag());
        } else {
            return;
        }

    }

    // transport error
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Manager Server transport error : " + session + ", exception : " + exception);
    }
}
