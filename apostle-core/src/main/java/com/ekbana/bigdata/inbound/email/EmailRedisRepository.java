package com.ekbana.bigdata.inbound.email;

import com.ekbana.bigdata.entity.emails.EmailEntry;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmailRedisRepository {
    private static final String EMAIL_KEYSPACE = "emails";

    private final RedisTemplate<String,Object> redisTemplate;
    private final HashOperations<String,String,Object> hashOperations;

    public EmailRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void registerEmail(EmailEntry email) {
        this.hashOperations.put(EMAIL_KEYSPACE,email.getKey(),email);
    }

    public EmailEntry getEmail(String key) {
        return (EmailEntry) this.hashOperations.get(EMAIL_KEYSPACE,key);
    }
}
