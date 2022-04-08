package com.ekbana.bigdata.inbound.rate;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Repository
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;

    public RateLimitService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public Integer get(String key, String interval) {
        return getTTL(key, interval) > 0 ?
                (Integer) valueOperations.get(key + "_" + interval)
                : 0;
    }

    public void update(String key, String interval) {
        if (getTTL(key,interval)>0){
            this.valueOperations.increment(key+"_"+interval);
        }else {
            this.valueOperations.set(key+"_"+interval,1);
            this.redisTemplate.expire(key+"_"+interval, Duration.ofSeconds(intervalToTimeStamp(interval)));
        }
    }

    private Long getTTL(String key, String interval) {
        return redisTemplate.getExpire(key + "_" + interval);
    }

    private Long intervalToTimeStamp(String interval){
        if(interval.equals(RateLimit.Interval.YEAR))
            return RateLimit.TIME_INTERVAL.YEAR;
        else if (interval.equals(RateLimit.Interval.MONTH))
            return RateLimit.TIME_INTERVAL.MONTH;
        else if (interval.equals(RateLimit.Interval.DAY))
            return RateLimit.TIME_INTERVAL.DAY;
        else if (interval.equals(RateLimit.Interval.HOUR))
            return RateLimit.TIME_INTERVAL.HOUR;
        else if (interval.equals(RateLimit.Interval.MINUTE))
            return RateLimit.TIME_INTERVAL.MINUTE;
        else return 0L;
    }

}
