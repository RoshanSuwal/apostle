package com.ekbana.bigdata.inbound.rate;

import com.ekbana.bigdata.entity.funnel.Funnel;
import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;

@com.ekbana.bigdata.annotation.Policy(value = "rate limit policy")
public class RateLimitPolicy extends Policy {

    // redis funnel dependency
    private final RateLimitService rateLimitService;

    @Autowired
    public RateLimitPolicy(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void pre(RequestWrapper requestWrapper) {
        String uniqueId = requestWrapper.getKeyClientApi().getUniqueId();
        Funnel funnel = requestWrapper.getKeyClientApi().getPersonalPackage().getFunnel();

        System.out.println(funnel);
        if (funnel==null) return;
        else if (funnel.getYear() > 0 && rateLimitService.get(uniqueId ,RateLimit.Interval.YEAR) >= funnel.getYear()) {
            // year rule
            throw new RateLimitException("year quota exceeded",requestWrapper);
        } else if (funnel.getMonth() > 0 && rateLimitService.get(uniqueId , RateLimit.Interval.MONTH) >= funnel.getMonth()) {
            // month rule
            throw new RateLimitException("month quota exceeded",requestWrapper);
        } else if (funnel.getDay() > 0 && rateLimitService.get(uniqueId , RateLimit.Interval.DAY) >= funnel.getDay()) {
            // day
            throw new RateLimitException("day quota exceeded",requestWrapper);
        } else if (funnel.getHour() > 0 && rateLimitService.get(uniqueId , RateLimit.Interval.HOUR) >= funnel.getHour()) {
            // hour
            throw new RateLimitException("hour quota exceeded",requestWrapper);
        } else if (funnel.getMinute() > 0 && rateLimitService.get(uniqueId , RateLimit.Interval.MINUTE) >= funnel.getMinute()) {
            // min
            throw new RateLimitException("minute quota exceeded",requestWrapper);
        }
    }

    @Override
    protected void post(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        String uniqueId = requestWrapper.getKeyClientApi().getUniqueId();
        Funnel funnel = requestWrapper.getKeyClientApi().getPersonalPackage().getFunnel();

        if (funnel==null) return;
        else if (funnel.getYear() > 0 ) {
            // year rule
            rateLimitService.update(uniqueId,RateLimit.Interval.YEAR);
        }

        if (funnel.getMonth() > 0 ) {
            // month rule
            rateLimitService.update(uniqueId,RateLimit.Interval.MONTH);
        }
        if (funnel.getDay() > 0 ) {
            // day rule
            rateLimitService.update(uniqueId,RateLimit.Interval.DAY);
        }
        if (funnel.getHour() > 0 ) {
            // hour rule
            rateLimitService.update(uniqueId,RateLimit.Interval.HOUR);
        }
        if (funnel.getMinute() > 0 ) {
            // minute rule
            rateLimitService.update(uniqueId,RateLimit.Interval.MINUTE);
        }
    }
}
