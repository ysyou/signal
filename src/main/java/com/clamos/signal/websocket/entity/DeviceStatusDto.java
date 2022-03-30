package com.clamos.signal.websocket.entity;

import lombok.Data;

@Data
public class DeviceStatusDto extends CommandDto {
    private Double cpuTemp;
    private float charging;
    private Double battery;
    private Double networkUp;
    private Double networkDown;
    private Double lat;
    private Double lon;
}
