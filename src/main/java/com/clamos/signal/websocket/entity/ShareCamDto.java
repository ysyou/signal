package com.clamos.signal.websocket.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import reactor.util.annotation.Nullable;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShareCamDto extends CommandDto {
    @Nullable
    private List<String> devices;
    private String source;
}
