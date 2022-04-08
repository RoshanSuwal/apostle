package com.ekbana.bigdata.inbound.rate;

import com.ekbana.bigdata.exception.BaseException;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.http.HttpStatus;

public class RateLimitException extends BaseException {

    public RateLimitException(String message, RequestWrapper requestWrapper) {
        super(message, HttpStatus.FORBIDDEN, requestWrapper);
    }
}
