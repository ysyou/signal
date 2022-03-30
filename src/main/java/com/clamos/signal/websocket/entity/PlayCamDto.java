package com.clamos.signal.websocket.entity;

import lombok.Data;

@Data
public class PlayCamDto extends CommandDto {
    private String source;
    private String offer;
}
