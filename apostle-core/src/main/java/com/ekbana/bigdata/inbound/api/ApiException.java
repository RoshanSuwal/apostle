package com.ekbana.bigdata.inbound.api;

import com.ekbana.bigdata.exception.BaseException;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.http.HttpStatus;

public class ApiException extends BaseException {
    public ApiException(String message, RequestWrapper requestWrapper) {
        super(message, HttpStatus.BAD_REQUEST, requestWrapper);
    }
}
