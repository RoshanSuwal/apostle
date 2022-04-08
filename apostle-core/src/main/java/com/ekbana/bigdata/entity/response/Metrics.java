package com.ekbana.bigdata.entity.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Metrics {
    private String apiUid;
    private String baseUrl;
    private String method;
    private String endPoint;
    private String ip;
    private String userAgent;
    private long time;

    private String publicKey;
    private String clientSid;

    private int statusCode;
    private long DoBR;
    private String responseType;

    public static final String RESPONSE_TYPE_CACHE = "cache";
    public static final String RESPONSE_TYPE_MOCK = "mock";
    public static final String RESPONSE_TYPE_BACKEND = "backend server";
    public static final String RESPONSE_TYPE_ERROR = "error";

    private static Logger metrics_logger = LoggerFactory.getLogger("metrics");
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static void log(Metrics metrics) throws JsonProcessingException {
        metrics_logger.info(objectMapper.writeValueAsString(metrics));
    }
}
