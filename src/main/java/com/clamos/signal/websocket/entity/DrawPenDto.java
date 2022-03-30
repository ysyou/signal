package com.clamos.signal.websocket.entity;

import lombok.Data;

@Data
public class DrawPenDto extends CommandDto {
    private String source;
    private String type;
    private DrawPenItemDto data;
}
