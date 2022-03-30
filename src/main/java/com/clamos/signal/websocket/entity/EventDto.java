package com.clamos.signal.websocket.entity;

import lombok.Data;

@Data
public class EventDto extends CommandDto {
    private Object data;

}
