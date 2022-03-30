package com.clamos.signal.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CommonService {
    public boolean getBoolean(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            Exception e = new Exception("[" + key + "] 키가 존재하지 않습니다.");
            log.error(e.getMessage());
        }
        return Boolean.parseBoolean(value.toString());
    }

    public String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            Exception e = new Exception("[" + key + "] 키가 존재하지 않습니다.");
            log.error(e.getMessage());
        }
        return value.toString();
    }

    public String getId(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            value = "";
        }
        return value.toString();
    }

    public float getFloat(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            Exception e = new Exception("[" + key + "] 키가 존재하지 않습니다.");
            log.error(e.getMessage());
        }
        return Float.parseFloat(value.toString());
    }

    public double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            Exception e = new Exception("[" + key + "] 키가 존재하지 않습니다.");
            log.error(e.getMessage());
        }
        return Double.parseDouble(value.toString());
    }

    public int getInt(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            Exception e = new Exception("[" + key + "] 키가 존재하지 않습니다.");
            log.error(e.getMessage());
        }
        return Integer.parseInt(value.toString());
    }

    public long getLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            Exception e = new Exception("[" + key + "] 키가 존재하지 않습니다.");
            log.error(e.getMessage());
        }
        return Long.parseLong(value.toString());
    }

    public Map<String, Object> getMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            Exception e = new Exception("[" + key + "] 키가 존재하지 않습니다.");
            log.error(e.getMessage());
        }
        return (Map<String, Object>)value;
    }

    public List<Map<String, Object>> getList(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            Exception e = new Exception("[" + key + "] 키가 존재하지 않습니다.");
            log.error(e.getMessage());
        }
        return (List<Map<String, Object>>)value;
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }
}
