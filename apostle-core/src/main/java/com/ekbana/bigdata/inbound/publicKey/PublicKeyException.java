package com.ekbana.bigdata.inbound.publicKey;

import com.ekbana.bigdata.exception.BaseException;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.http.HttpStatus;

public class PublicKeyException extends BaseException {
    public PublicKeyException(String message,RequestWrapper requestWrapper) {
        super(message, HttpStatus.FORBIDDEN, requestWrapper);
    }
}
