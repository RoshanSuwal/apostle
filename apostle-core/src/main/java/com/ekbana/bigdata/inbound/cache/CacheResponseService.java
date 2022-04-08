package com.ekbana.bigdata.inbound.cache;

import com.ekbana.bigdata.entity.api.Api;
import com.ekbana.bigdata.entity.cache.CacheResponse;
import com.ekbana.bigdata.entity.response.Metrics;
import com.ekbana.bigdata.exception.BaseException;
import com.ekbana.bigdata.helpers.UrlComponents;
import com.ekbana.bigdata.response.ResponseService;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@com.ekbana.bigdata.annotation.ResponseService(value = "cache response")
public class CacheResponseService extends ResponseService {
    @Autowired
    private CachingService cachingService;

    private AntPathMatcher antPathMatcher=new AntPathMatcher();

    public CacheResponseService() {
        super("CACHE RESPONSE");
    }

    @Override
    protected ResponseEntity<?> responseEntity(RequestWrapper request) {
        if (request.getKeyClientApi().getApi().isCacheable()) {
            Map<String, String> stringStringMap = antPathMatcher.extractUriTemplateVariables(request.getKeyClientApi().getApi().getParameters(), request.getApi().getParameters());
            CacheResponse cacheResponse = new CacheResponse(request.getKeyClientApi().getUniqueId(),
                    keyForCaching(request.getKeyClientApi().getApi(),request.getUrlComponents()),
                    request.getKeyClientApi().getApi().getCachePeriod(),
                    Instant.now().getEpochSecond());

            if (cachingService.isResponseAvailableInCache(cacheResponse)) {
                return ResponseEntity.status(HttpStatus.OK).body(cachingService.getResponseFromCache(cacheResponse));
            }
        }
        return null;
    }

    private String keyForCaching(Api api, UrlComponents urlComponents){
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

        //TODO : perform some hashing algorithm to cachingKey
        return cachingKey.toString();
    }

    private void cacheResponse(RequestWrapper request,ResponseEntity<?> responseEntity){
        if (request.getKeyClientApi().getApi().isCacheable() && responseEntity.getStatusCode()==HttpStatus.OK) {
            CacheResponse cacheResponse = new CacheResponse(request.getKeyClientApi().getUniqueId(),
                    keyForCaching(request.getKeyClientApi().getApi(),request.getUrlComponents()),
                    request.getKeyClientApi().getApi().getCachePeriod(),
                    Instant.now().getEpochSecond());
            cachingService.cacheResponse(cacheResponse, Objects.requireNonNull(responseEntity.getBody()).toString());
            logger.info("Caching the response");
        }
    }

    @Override
    public ResponseWrapper response(RequestWrapper request) {
        logger.info("[{}] {}","Response Service", this.getClass().getSimpleName());
        ResponseEntity<?> responseEntity = responseEntity(request);
        if (responseEntity!=null) {
            log(request);
            return ResponseWrapper.builder()
                    .responseType(Metrics.RESPONSE_TYPE_CACHE)
                    .responseEntity(responseEntity)
                    .executionTime(Instant.now().toEpochMilli() - serviceStartTime)
                    .build();
        }else if (this.nextService!=null){
            ResponseWrapper response = this.nextService.response(request);
            cacheResponse(request,response.getResponseEntity());
            return response;
        }else {
            throw new BaseException("Page not found",HttpStatus.SERVICE_UNAVAILABLE,request);
        }
    }
}
