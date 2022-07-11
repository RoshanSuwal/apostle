package com.ekbana.bigdata.inbound.quota;

import com.ekbana.bigdata.entity.calls.Grace;
import com.ekbana.bigdata.entity.calls.Paid;
import com.ekbana.bigdata.entity.publickey.ExpiredPublicKey;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class QuotaService {
    public static final String PAID_KEYSPACE = "paid_calls";
    public static final String GRACE_KEYSPACE = "grace_calls";
    public static final String EXPIRE_KEY_SPACE = "expire_keys";

    private final RedisTemplate<String,Object> redisTemplate;
    private final HashOperations<String,String,Object> hashOperations;

    public QuotaService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations=redisTemplate.opsForHash();
    }

    public boolean isInPaidKeySpace(String key){
        return this.hashOperations.hasKey(PAID_KEYSPACE,key);
    }

    public boolean isInGraceKeySpace(String key){
        return this.hashOperations.hasKey(GRACE_KEYSPACE,key);
    }

    public void registerInPaidKeySpace(Paid paid){
        this.hashOperations.put(PAID_KEYSPACE,paid.getUuid(),paid);
    }

    public Paid getFromPaidKeyspace(String key){
        return (Paid) this.hashOperations.get(PAID_KEYSPACE,key);
    }
    public void increasePaidCallUses(Paid paid){
        paid.setUsed_calls(paid.getUsed_calls()+1);
        registerInPaidKeySpace(paid);
    }

    public void registerInPaidKeySpace(String key){
        this.hashOperations.put(PAID_KEYSPACE,key,0);
    }

    public void increasePaidCallUses(String key){
        this.hashOperations.increment(PAID_KEYSPACE,key,1);
    }
    public Integer getFromPaidKeySpace(String key){
        return (Integer) this.hashOperations.get(PAID_KEYSPACE,key);
    }

    public void registerInGraceKeySpace(Grace grace){
        this.hashOperations.put(GRACE_KEYSPACE,grace.getUuid(),grace);
    }

    public Grace getFromGraceKeyspace(String key){
        return (Grace) this.hashOperations.get(GRACE_KEYSPACE,key);
    }

    public void registerInGraceKeySpace(String key){
        this.hashOperations.put(GRACE_KEYSPACE,key,0);
    }
    public Integer getFromGraceKeySpace(String key){
        return (Integer) this.hashOperations.get(GRACE_KEYSPACE,key);
    }

    public void increaseGraceCallUses(String key){
        this.hashOperations.increment(GRACE_KEYSPACE,key,1);
    }

    public void increaseGraceCallUses(Grace grace){
        grace.setUsed_grace_calls(grace.getUsed_grace_calls()+1);
        registerInGraceKeySpace(grace);
    }

    public void registerInExpireKeySpace(ExpiredPublicKey expiredPublicKey){
        this.hashOperations.put(EXPIRE_KEY_SPACE,expiredPublicKey.getPublicKey(),expiredPublicKey);
    }
}
