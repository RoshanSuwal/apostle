package com.ekbana.bigdata.exception;

import com.ekbana.bigdata.configuration.ApiHandler;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log= LoggerFactory.getLogger("error-metrics");
    private final ApiHandler apiHandler;

    @Autowired
    public ExceptionResponseController(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<?> handleBaseException(BaseException baseException) {
        log.error("\n[ API Detail ]\n[ requested ] : {}\n\n[ actual ] : {}",
                baseException.getRequestWrapper().getApi().toString(),
                baseException.getRequestWrapper().getKeyClientApi()!=null?baseException.getRequestWrapper().getKeyClientApi().getApi():null,
                baseException);

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
