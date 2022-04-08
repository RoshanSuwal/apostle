package com.ekbana.bigdata.inbound.personalpackage;

import com.ekbana.bigdata.entity.publickey.ExpiredPublicKey;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PackageService {
    private static final String EXPIRE_KEY_SPACE = "expire_keys";

    private final RedisTemplate<String,Object> redisTemplate;
    private final HashOperations<String,String,Object> hashOperations;

    public PackageService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations= redisTemplate.opsForHash();
    }

    public void registerInExpiredKeySpace(ExpiredPublicKey expiredPublicKey){
        this.hashOperations.put(EXPIRE_KEY_SPACE,expiredPublicKey.getPublicKey(),expiredPublicKey);
    }

    public boolean isInExpiredKeySpace(String key){
        return this.hashOperations.hasKey(EXPIRE_KEY_SPACE,key);
    }
}
