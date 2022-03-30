package com.clamos.signal.common.service;

import com.clamos.signal.websocket.handler.WebSocketDeviceHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CommonService cs;

    @Autowired
    WebSocketDeviceHandler webSocketDeviceHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String payload = new String(message.getBody(), StandardCharsets.UTF_8);
            String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
            Map map = new ObjectMapper().readValue(payload, Map.class);
            switch (channel){
                case "webStartCam" :
                log.info("WebStartCam signal ->  mobile  paylaod : ", payload);
                List deviceList = (ArrayList)map.get("devices");
                for(int i =0; i <deviceList.size();i++){
                    Map resultMap = new HashMap();
                    resultMap.put("cmd",map.get("cmd"));
                    resultMap.put("tag",cs.getUUID());
                    resultMap.put("device",(String)deviceList.get(i));

                    webSocketDeviceHandler.sendToEvent(resultMap);

                }




                    break;
                case "test" :

                    break;
                default:

            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
