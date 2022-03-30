package com.clamos.signal.websocket.entity;

import lombok.Data;

import java.util.List;

@Data
public class WebCamDto extends CommandDto {
    private List<String> devices;
}
