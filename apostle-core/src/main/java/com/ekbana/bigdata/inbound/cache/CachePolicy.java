package com.ekbana.bigdata.inbound.cache;

import com.ekbana.bigdata.entity.cache.CacheResponse;
import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Objects;

@com.ekbana.bigdata.annotation.Policy(value = "cache policy")
public class CachePolicy extends Policy {
    private final CachingService cachingService;

    @Autowired
    public CachePolicy(CachingService cachingService) {
        this.cachingService = cachingService;
    }

    @Override
    protected void post(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        if (requestWrapper.getKeyClientApi().getApi().isCacheable()) {
            if (responseWrapper.getResponseType().equals("BACKEND") && responseWrapper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                CacheResponse cacheResponse = new CacheResponse(requestWrapper.getKeyClientApi().getUniqueId(),
                        cachingService.keyForCaching(requestWrapper.getKeyClientApi().getApi(), requestWrapper.getUrlComponents()),
                        requestWrapper.getKeyClientApi().getApi().getCachePeriod(),
                        Instant.now().getEpochSecond());
                cachingService.cacheResponse(cacheResponse, Objects.requireNonNull(responseWrapper.getResponseEntity().getBody()).toString());
                log.info("Caching the response");
            }
        }
    }
}
