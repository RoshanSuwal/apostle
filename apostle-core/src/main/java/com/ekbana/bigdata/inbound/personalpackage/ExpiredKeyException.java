package com.ekbana.bigdata.inbound.personalpackage;

import com.ekbana.bigdata.exception.BaseException;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.http.HttpStatus;

public class ExpiredKeyException extends BaseException {

    public ExpiredKeyException(String message, RequestWrapper requestWrapper) {
        super(message, HttpStatus.FORBIDDEN, requestWrapper);
    }
}
