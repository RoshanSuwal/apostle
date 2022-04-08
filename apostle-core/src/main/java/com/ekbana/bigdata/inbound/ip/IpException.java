package com.ekbana.bigdata.inbound.ip;

import com.ekbana.bigdata.exception.BaseException;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.http.HttpStatus;

public class IpException extends BaseException {

    public IpException(String message, RequestWrapper requestWrapper) {
        super(message, HttpStatus.FORBIDDEN, requestWrapper);
    }
}
