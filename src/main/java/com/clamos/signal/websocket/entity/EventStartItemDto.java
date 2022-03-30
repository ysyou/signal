package com.clamos.signal.websocket.entity;

import lombok.Data;

@Data
public class EventStartItemDto extends EventDto{
    private String lv;
    private String caseNum;
    private String carNum;
    private String ts;
}
