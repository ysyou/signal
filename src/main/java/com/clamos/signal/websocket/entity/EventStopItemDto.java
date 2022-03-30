package com.clamos.signal.websocket.entity;

import lombok.Data;

@Data
public class EventStopItemDto extends EventDto{
    private String endLv;
    private String caseNum;
    private String carNum;
    private String ts;
}
