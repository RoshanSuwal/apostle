package com.ekbana.bigdata.response;

import com.ekbana.bigdata.exception.BaseException;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public abstract class ResponseService {
    protected final static Logger logger= LoggerFactory.getLogger(ResponseService.class);

    private final String responseType;
    protected ResponseService nextService=null;
    protected Long serviceStartTime=0L;

    public ResponseService(String responseType){
        this.responseType=responseType;
    }

    public ResponseService nextService(ResponseService responseHandler){
        this.nextService=responseHandler;
        return this.nextService;
    }

    protected abstract ResponseEntity<?> responseEntity(RequestWrapper request);

    protected void log(RequestWrapper request){
        logger.info(this.getClass().getSimpleName());
    }

    public ResponseWrapper response(RequestWrapper request){
        logger.info("[{}] {}","Response Service", this.getClass().getSimpleName());
        ResponseEntity<?> responseEntity = responseEntity(request);
        if (responseEntity!=null) {
            log(request);
            return ResponseWrapper.builder()
                    .responseType(responseType)
                    .responseEntity(responseEntity)
                    .executionTime(Instant.now().toEpochMilli() - serviceStartTime)
                    .build();
        }else if (nextService!=null) return nextService.response(request);
        else throw new BaseException("page not found", HttpStatus.SERVICE_UNAVAILABLE,request);
    }

}
