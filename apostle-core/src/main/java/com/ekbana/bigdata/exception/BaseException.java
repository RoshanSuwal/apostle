package com.ekbana.bigdata.exception;

import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final RequestWrapper requestWrapper;

    public BaseException(String message,HttpStatus httpStatus,RequestWrapper requestWrapper){
        super(message);
        this.httpStatus=httpStatus;
        this.requestWrapper=requestWrapper;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public RequestWrapper getRequestWrapper() {
        return requestWrapper;
    }
}
