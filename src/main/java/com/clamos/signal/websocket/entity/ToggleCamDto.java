package com.clamos.signal.websocket.entity;

import lombok.Data;

@Data
public class ToggleCamDto extends CommandDto{
    private String device;
}
