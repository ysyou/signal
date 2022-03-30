package com.clamos.signal.websocket.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import reactor.util.annotation.Nullable;

import javax.ws.rs.DefaultValue;

@Data
public class CommandDto {
    private String cmd;
    private String tag;

    @JsonProperty(defaultValue = "")
    private String id;
}
