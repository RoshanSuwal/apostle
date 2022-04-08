package com.ekbana.bigdata.exception;

import com.ekbana.bigdata.configuration.ApiHandler;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
@RestController
public class ExceptionResponseController extends ResponseEntityExceptionHandler {

    @Autowired
    ApiHandler apiHandler;

    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<?> handleBaseException(BaseException baseException) {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(ExceptionResponse.builder()
                .message(baseException.getMessage())
                .status_code(baseException.getHttpStatus().value())
                .build()
                , baseException.getHttpStatus());

        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .responseEntity(responseEntity)
                .responseType("ERROR")
                .executionTime(Instant.now().getEpochSecond()-baseException.getRequestWrapper().getStartTime())
                .build();

        apiHandler.getPostService().postServiceExecute(baseException.getRequestWrapper(),responseWrapper);

        return responseWrapper.getResponseEntity();
    }
}
