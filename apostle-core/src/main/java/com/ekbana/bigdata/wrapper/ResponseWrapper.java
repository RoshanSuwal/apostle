package com.ekbana.bigdata.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class ResponseWrapper {
    ResponseEntity<?> responseEntity;
    String responseType;
    Long executionTime;
}
