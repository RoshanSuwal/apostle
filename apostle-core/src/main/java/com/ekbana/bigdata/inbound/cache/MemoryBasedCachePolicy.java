package com.ekbana.bigdata.inbound.cache;

import com.ekbana.bigdata.entity.cache.CacheResponse;
import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Objects;

@com.ekbana.bigdata.annotation.Policy(value = "memory-based cache policy")
public class MemoryBasedCachePolicy extends Policy {
    private final CachingService cachingService;
    private final Long MAX_MEMORY_SIZE_FOR_KEY = 200L;// 10 bytes by default

    @Autowired
    public MemoryBasedCachePolicy(CachingService cachingService) {
        this.cachingService = cachingService;
    }

    @Override
    protected void post(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        if (requestWrapper.getKeyClientApi().getApi().isCacheable()) {
            if ((responseWrapper.getResponseType().equals("BACKEND")
//                    || responseWrapper.getResponseType().equals("MOCK")
            ) && responseWrapper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                // check the memory limit if exceed clear the key and insert new
                final long memorySizeUsedByKey = cachingService.getMemorySizeUsedByKey(requestWrapper.getKeyClientApi().getUniqueId());
                if (memorySizeUsedByKey >= MAX_MEMORY_SIZE_FOR_KEY) {
                    log.info("removing cache key : {}", requestWrapper.getKeyClientApi().getUniqueId());
                    cachingService.removeKey(requestWrapper.getKeyClientApi().getUniqueId());
                }

                CacheResponse cacheResponse = new CacheResponse(requestWrapper.getKeyClientApi().getUniqueId(),
                        cachingService.keyForCaching(requestWrapper.getKeyClientApi().getApi(), requestWrapper.getUrlComponents()),
                        requestWrapper.getKeyClientApi().getApi().getCachePeriod(),
                        Instant.now().getEpochSecond());
                cachingService.cacheResponse(cacheResponse, Objects.requireNonNull(responseWrapper.getResponseEntity().getBody()).toString());
                log.info("Caching the response");
                final long memorySizeUsedByKey1 = cachingService.getMemorySizeUsedByKey(requestWrapper.getKeyClientApi().getUniqueId());
                final long sizeOfCacheKey = cachingService.getSizeOfHashKey(requestWrapper.getKeyClientApi().getUniqueId());
                log.info("caching key : {} of size : {} with memory used : {}", requestWrapper.getKeyClientApi().getUniqueId(), sizeOfCacheKey,memorySizeUsedByKey1);
                requestWrapper.putInMetrics("memory_used_for_cache", memorySizeUsedByKey1);
                requestWrapper.putInMetrics("size_of_cache",sizeOfCacheKey);
            }
        }else {
            requestWrapper.putInMetrics("memory_used_for_cache", 0);
            requestWrapper.putInMetrics("size_of_cache",0);
        }
    }

}
