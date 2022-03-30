package com.clamos.signal.websocket.handler;

import com.clamos.signal.common.config.RedisKeys;
import com.clamos.signal.websocket.entity.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

@Slf4j
@Component
public class WebSocketWebHandler extends TextWebSocketHandler {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RedisTemplate redisTemplate;

    private static List<SessionDto> sessionList = new CopyOnWriteArrayList<>();

    // connection opened
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setSession(session);
        sessionList.add(sessionDto);
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
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        try {
            String payload = message.getPayload();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            CommandDto commandDto = objectMapper.readValue(payload, CommandDto.class);
            ResponseDto res = null;

            if(commandDto.getId() == null){
                commandDto.setId("");
            }

            Long count = sessionList.stream().filter(x -> commandDto.getId().equals(x.getId())).count();

            //기존로그인이 된
            if(count > 0){
                if(commandDto.getCmd().equals("mediaList")){
                    //

                    res = new MediaListDto();

                }else if(commandDto.getCmd().equals("webStartCam")){
                    log.info("webStartCam : web -> signal message payload : " + payload);
                    redisTemplate.convertAndSend(RedisKeys.WEBS_START_CAM, objectMapper.writeValueAsString(objectMapper.readValue(payload, Map.class)));
                    res = new ResponseDto();
                    res.setCmd(commandDto.getCmd());
                    res.setTag(commandDto.getTag());
                    res.setCode(0);
                    res.setMsg("");
                }else if(commandDto.getCmd().equals("webStopCam")){

                }else if(commandDto.getCmd().equals("playCam")){







                }else if(commandDto.getCmd().equals("shareCam")){

                }else if(commandDto.getCmd().equals("shareImg")){

                }else if(commandDto.getCmd().equals("toggleCam")){

                }else if(commandDto.getCmd().equals("drawPen")){

                }else if(commandDto.getCmd().equals("login")){
                    res = new ResponseDto();
                    res.setCmd(commandDto.getCmd());
                    res.setTag(commandDto.getTag());
                    res.setCode(0);
                    res.setMsg("success");
                }else {
                    return;
                }
            }else{
                if(commandDto.getCmd().equals("login")){
                    //세션 리스트에 id값 추가
                    IntStream.range(0, sessionList.size()).filter(i -> sessionList.get(i).getSession().getId().equals(session.getId())).forEachOrdered(i -> sessionList.get(i).setId(commandDto.getId()));

                    res = new ResponseDto();
                    res.setCmd(commandDto.getCmd());
                    res.setTag(commandDto.getTag());
                    res.setCode(0);
                    res.setMsg("success");
                } else if(commandDto.getCmd().equals("keepAlive")){ // 핑퐁이되면 삭제
                    res = new ResponseDto();
                    res.setCmd(commandDto.getCmd());
                    res.setTag(commandDto.getTag());
                }else{  // code와 msg 작성 필요
                    res = new ResponseDto();
                    res.setCode(1);
                    res.setMsg("로그인이 필요합니다.");
                }
            }
            if(res != null){
                String responseMessage = objectMapper.writeValueAsString(res);
                send(session,responseMessage);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }


    public void send(WebSocketSession session, String payload) {
        try {
            log.info("send payload: {}", payload);
            synchronized (session) {
                TextMessage message = new TextMessage(payload);
                session.sendMessage(message);
            }
        } catch (IOException e) {
            log.error("send error: {}", e.getMessage());
        }
    }

    public void sendToEvent(Map payload) throws IOException {

        for (SessionDto sessionDto : sessionList) {
            /*if (sessionDto.getSession().isOpen()) {
            } else {
                *//*log.info("session disconnected : " + sessionDto.getSession());
                sessionList.remove(sessionDto.getSession());*//*
            }*/
        }
    }

    // transport error
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Web Server transport error : " + session + ", exception : " + exception);
    }

}
