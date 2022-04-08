package com.ekbana.bigdata.inbound.ip;

import com.ekbana.bigdata.entity.ip.MarkedIP;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class IpService {

    private static final String BLACKLIST_IP_KEYSPACE = "blacklisted_ip";
    private static final String MARKED_IP_KEYSPACE = "marked_ip";

    private final RedisTemplate<String,Object> redisTemplate;
    private final HashOperations<String,String,Object> hashOperations;

    public IpService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void blackListIp(MarkedIP markedIP){
        this.hashOperations.put(BLACKLIST_IP_KEYSPACE,markedIP.getIp_addr(),markedIP);
    }

    public boolean isBlackListed(String ipAddress){
        return this.hashOperations.hasKey(BLACKLIST_IP_KEYSPACE,ipAddress);
    }

    public void markIp(String ipAddress){
        this.hashOperations.put(MARKED_IP_KEYSPACE,ipAddress,ipAddress);
    }

    public boolean isMarked(String ipAddress){
        return this.hashOperations.hasKey(MARKED_IP_KEYSPACE,ipAddress);
    }


}
