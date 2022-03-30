package com.clamos.signal.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

//    public void listKeys(){
//        try {
//            redisTemplate.hasKey();
//        } catch (Exception e) {
//            log.error("### Redis Set Key Error !!! ::: {}", e.getMessage());
//        }
//    }

    // set이 아니기에 키가 존재하면 값을 overwrite 함.
    public void addKey(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("### Redis Set Key Error !!! ::: ", e);
        }
    }

    public String getValue(String key) {
        String value = "";
        try {
            if (redisTemplate.hasKey(key)) {
                value = redisTemplate.opsForValue().get(key);
            }
        } catch (Exception e) {
            log.error("### Redis Get Key Error !!! :::", e);
        }
        return value;
    }

}
