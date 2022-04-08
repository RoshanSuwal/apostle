package com.ekbana.bigdata.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {
    @Builder.Default
    private String message = "Sorry, Something went wrong";
    private int status_code;
    @Builder.Default
    private ResponseMeta meta=new ResponseMeta();
}
