package com.securevault.service;

import org.apache.coyote.RequestInfo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {
    private final Map<String, RequestInfo> requests = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS =5;
    private static final long TIME_WINDOW= 60_000; //1 min

    public boolean isAllowed(String key){
        long currentTime= System.currentTimeMillis();

        RequestInfo info = requests.get(key);
        if(info == null || currentTime- info.startTime>= TIME_WINDOW){
            requests.put(key, new RequestInfo(currentTime, 1));
            return true;
        }
        if(info.count >= MAX_REQUESTS){
            return false;
        }
        info.count++;
        return true;
    }
    private static class RequestInfo{
        long startTime;
        int count;
        RequestInfo(long startTime, int count){
            this.startTime= startTime;
            this.count= count;
        }
    }

}
