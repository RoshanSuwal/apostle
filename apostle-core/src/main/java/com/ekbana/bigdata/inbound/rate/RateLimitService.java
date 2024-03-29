package com.ekbana.bigdata.inbound.rate;

import com.ekbana.bigdata.configuration.ApplicationConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Repository
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;
    private final ApplicationConfiguration applicationConfiguration;

    public RateLimitService(RedisTemplate<String, Object> redisTemplate, ApplicationConfiguration applicationConfiguration) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.applicationConfiguration = applicationConfiguration;
    }

    public Integer get(String key, String interval) {
        return getTTL(key, interval) > 0 ?
                (Integer) valueOperations.get(key + "_" + interval)
                : 1;
    }

    public void update(String key, String interval,long rate) {
        if (getTTL(key,interval)>0){
            this.valueOperations.decrement(key+"_"+interval,1);
        }else {
            this.valueOperations.set(key+"_"+interval,rate-1);
            this.redisTemplate.expire(key+"_"+interval, Duration.ofSeconds(intervalToTimeStamp(interval)));
        }
    }

    private Long getTTL(String key, String interval) {
        return redisTemplate.getExpire(key + "_" + interval);
    }

    private Long intervalToTimeStamp(String interval){
        Long currentEpochSecond = OffsetDateTime.now(ZoneOffset.of(applicationConfiguration.getTIMEZONE_OFFSET())).toEpochSecond();
        if(interval.equals(RateLimit.Interval.YEAR))
            return OffsetDateTime.now(ZoneOffset.of(applicationConfiguration.getTIMEZONE_OFFSET())).plusYears(1).withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).toEpochSecond()-currentEpochSecond;
//            return RateLimit.TIME_INTERVAL.YEAR;
        else if (interval.equals(RateLimit.Interval.MONTH))
            return OffsetDateTime.now(ZoneOffset.of(applicationConfiguration.getTIMEZONE_OFFSET())).plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).toEpochSecond()-currentEpochSecond;
//        return RateLimit.TIME_INTERVAL.MONTH;
        else if (interval.equals(RateLimit.Interval.DAY))
            return OffsetDateTime.now(ZoneOffset.of(applicationConfiguration.getTIMEZONE_OFFSET())).plusDays(1).withHour(0).withMinute(0).withSecond(0).toEpochSecond()-currentEpochSecond;
//        return RateLimit.TIME_INTERVAL.DAY;
        else if (interval.equals(RateLimit.Interval.HOUR))
            return OffsetDateTime.now(ZoneOffset.of(applicationConfiguration.getTIMEZONE_OFFSET())).plusHours(1).withMinute(0).withSecond(0).toEpochSecond()-currentEpochSecond;
//        return RateLimit.TIME_INTERVAL.HOUR;
        else if (interval.equals(RateLimit.Interval.MINUTE))
            return RateLimit.TIME_INTERVAL.MINUTE;
        else return 0L;
    }

}
