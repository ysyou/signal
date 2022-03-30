package com.clamos.signal.websocket.entity;

import lombok.Data;

import java.util.List;

@Data
public class MediaListDto extends ResponseDto{
    private List<MediaItemDto> list;
}
