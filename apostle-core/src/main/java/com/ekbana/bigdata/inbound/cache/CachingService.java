package com.ekbana.bigdata.inbound.cache;

import com.ekbana.bigdata.entity.cache.CacheResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Repository
public class CachingService {

    private final RedisTemplate<String,Object> redisTemplate;
    private final HashOperations<String,String ,Object> hashOperations;

    @Autowired
    public CachingService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations=redisTemplate.opsForHash();
    }

    public boolean isResponseAvailableInCache(CacheResponse cacheResponse){
        return hashOperations.hasKey(cacheResponse.getKey(),cacheResponse.getHashKey());
    }

    public String getResponseFromCache(CacheResponse cacheResponse){
        return hashOperations.get(cacheResponse.getKey(),cacheResponse.getHashKey()).toString();
    }

    public void cacheResponse(CacheResponse cacheResponse,String cacheBody){
        if (!this.redisTemplate.hasKey(cacheResponse.getKey())){
            this.hashOperations.put(cacheResponse.getKey(), cacheResponse.getHashKey(), cacheBody);
            this.redisTemplate.expire(cacheResponse.getKey(), Duration.ofSeconds(cacheResponse.getTtl()));
        }else {
            this.hashOperations.put(cacheResponse.getKey(), cacheResponse.getHashKey(), cacheBody);
        }
    }
}
