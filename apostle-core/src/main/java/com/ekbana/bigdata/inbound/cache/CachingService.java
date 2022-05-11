package com.ekbana.bigdata.inbound.cache;

import com.ekbana.bigdata.entity.api.Api;
import com.ekbana.bigdata.entity.cache.CacheResponse;
import com.ekbana.bigdata.helpers.UrlComponents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Repository
public class CachingService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;

    @Autowired
    public CachingService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    public long getSizeOfHashKey(String key){
        return this.hashOperations.size(key);
    }
    public long getMemorySizeUsedByKey(String key) {
        return this.redisTemplate.hasKey(key) ? getMemoryUsages(key) : 0L;
    }

    private Long getMemoryUsages(String key){
        String script="return redis.pcall('MEMORY','USAGE','"+key+"')";
        final Object o = this.redisTemplate.executePipelined((RedisCallback<Long>) connection -> {
            connection.eval(script.getBytes(StandardCharsets.UTF_8),
                    ReturnType.INTEGER,
                    1,
                    key.getBytes(StandardCharsets.UTF_8));
            return null;
        }).get(0);
        return (Long) o;
    }

    public void removeKey(String key){
        this.redisTemplate.delete(key);
    }

    public boolean isResponseAvailableInCache(CacheResponse cacheResponse) {
        return hashOperations.hasKey(cacheResponse.getKey(), cacheResponse.getHashKey());
    }

    public String getResponseFromCache(CacheResponse cacheResponse) {
        return hashOperations.get(cacheResponse.getKey(), cacheResponse.getHashKey()).toString();
    }

    public void cacheResponse(CacheResponse cacheResponse, String cacheBody) {
        if (!this.redisTemplate.hasKey(cacheResponse.getKey())) {
            this.hashOperations.put(cacheResponse.getKey(), cacheResponse.getHashKey(), cacheBody);
            this.redisTemplate.expire(cacheResponse.getKey(), Duration.ofSeconds(cacheResponse.getTtl()));
        } else {
            this.hashOperations.put(cacheResponse.getKey(), cacheResponse.getHashKey(), cacheBody);
        }
    }

    public String keyForCaching(Api api, UrlComponents urlComponents) {
        StringBuilder cachingKey = new StringBuilder();
        if (api.getHeader_params_for_caching() != null) {
            for (String str : api.getHeader_params_for_caching().split(",")) {
                cachingKey.append(str)
                        .append("=")
                        .append(urlComponents.getHeaderKey(str)).append(",");
            }
        }

        if (api.getQueries_params_for_caching() != null) {
            for (String str : api.getQueries_params_for_caching().split(",")) {
                cachingKey.append(str)
                        .append("=")
                        .append(urlComponents.getParameterByKey(str)).append(",");
            }
        }

        if (api.getPath_variables_for_caching() != null) {
            for (String str : api.getPath_variables_for_caching().split(",")) {
                cachingKey.append(str)
                        .append("=")
                        .append(urlComponents.getPathVariableByKey(str)).append(",");
            }
        }
        //TODO : perform some hashing algorithm to cachingKey
        return cachingKey.toString();
    }
}
