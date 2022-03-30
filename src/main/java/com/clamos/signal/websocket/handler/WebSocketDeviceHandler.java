package com.clamos.signal.websocket.handler;

import com.clamos.signal.common.service.CommonService;
import com.clamos.signal.websocket.entity.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.util.ConcurrentBag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

@Slf4j
@Component
public class WebSocketDeviceHandler extends TextWebSocketHandler {


    @Autowired
    ObjectMapper objectMapper;

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
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            CommandDto commandDto = objectMapper.readValue(payload, CommandDto.class);
            ResponseDto res = null;

            if(commandDto.getId() == null){
                commandDto.setId("");
            }

            Long count = sessionList.stream().filter(x -> commandDto.getId().equals(x.getId())).count();

            //기존로그인이 된
            if(count > 0){
                if(commandDto.getCmd().equals("playCam")){
                    PlayCamDto playCamDto =  objectMapper.readValue(payload, PlayCamDto.class);


                    //미디어서버 가서 데이터 주고 받고
                    //받은값을 다시 return하는 구조
                } else if(commandDto.getCmd().equals("deviceStatus")){
                    DeviceStatusDto deviceStatusDto =  objectMapper.readValue(payload, DeviceStatusDto.class);

                    //받은 데이터를 웹서버에게 전달 (규격하나 더 추가해야됨)
                    res = new ResponseDto();
                    res.setCmd(commandDto.getCmd());
                    res.setTag(commandDto.getTag());
                    res.setCode(0);
                    res.setMsg("success");
                } else if(commandDto.getCmd().equals("drawPen")){
                    DrawPenDto drawPenDto = objectMapper.readValue(payload, DrawPenDto.class);


                    //다르게 처리할일이 없음  -> 바로 web서버에 보내주면 그만~
                    /*if(drawPenDto.getType().equals("add")){

                    }else if(drawPenDto.getType().equals("undo")){

                    }else if(drawPenDto.getType().equals("redo")){

                    }else if(drawPenDto.getType().equals("clear")){

                    }*/


                } else if(commandDto.getCmd().equals("login")){
                    res = new ResponseDto();
                    res.setCmd(commandDto.getCmd());
                    res.setTag(commandDto.getTag());
                    res.setCode(1);
                    res.setMsg("success");

                }  else if(commandDto.getCmd().equals("webStartCam")){
                    log.info("WebStartCam Response device -> signal  payload : " , payload );
                    //실패했을때 처리
                }
            }else{
                if(commandDto.getCmd().equals("login")){
                    //디비 조회한 후 맞으면 ConcurrentHashMap 에 put
                    // 디비조회

                    //if 있으면 result = success, 없으면 result

                    //세션 리스트에 id값 추가
                    IntStream.range(0, sessionList.size()).filter(i -> sessionList.get(i).getSession().getId().equals(session.getId())).forEachOrdered(i -> sessionList.get(i).setId(commandDto.getId()));

                    res = new ResponseDto();
                    res.setCmd(commandDto.getCmd());
                    res.setTag(commandDto.getTag());
                    res.setCode(1);
                    res.setMsg("success");
                } else if(commandDto.getCmd().equals("keepAlive")){ // 핑퐁이되면 삭제
                    res = new ResponseDto();
                    res.setCmd(commandDto.getCmd());
                    res.setTag(commandDto.getTag());
                }else{  // code와 msg 작성 필요
                    res = new ResponseDto();
                    res.setCode(401);
                    res.setMsg("로그인이 필요합니다.");
                }
            }
            if(res != null){
                String responseMessage = objectMapper.writeValueAsString(res);
                send(session,responseMessage);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            //에러가나면 보낼 메시지
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

    public void sendToEvent(Map map) throws IOException {
        for (SessionDto sessionDto : sessionList) {
            if(sessionDto.getId().equals(map.get("device"))){
                String responseMessage = objectMapper.writeValueAsString(map);
                send(sessionDto.getSession(), responseMessage);
            } else {
                log.info("session disconnected : " + sessionDto.getSession());
                if(sessionDto.getId().equals(map.get("device"))){
                    sessionList.remove(sessionDto);
                }
            }
        }
    }

    // transport error
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Device transport error : " + session + ", exception : " + exception);
    }
}
