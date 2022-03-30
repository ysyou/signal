package com.clamos.signal.websocket.entity;

import lombok.Data;

import java.util.List;

@Data
public class DrawPenItemDto {
    private String color;
    private String weight;
    private List<CoordinateDto> points;
}
