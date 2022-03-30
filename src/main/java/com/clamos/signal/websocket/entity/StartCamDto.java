package com.clamos.signal.websocket.entity;

import lombok.Data;

@Data
public class StartCamDto extends CommandDto{
    private String offer;
}
