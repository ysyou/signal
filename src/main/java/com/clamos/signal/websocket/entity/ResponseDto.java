package com.clamos.signal.websocket.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import reactor.util.annotation.Nullable;

@Data
public class ResponseDto {
    private String cmd;
    private String tag;
    private Integer code;
    private String msg;

}
