package com.ekbana.bigdata.inbound.metric;

import com.ekbana.bigdata.post.PostService;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@com.ekbana.bigdata.annotation.PostService(value = "metrics service")
public class MetricService extends PostService {

    private static final Logger log= LoggerFactory.getLogger("metric");
    public static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void execute(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {

        try {
            log.info(
                    objectMapper.writeValueAsString(
                            Metrics.builder()
                                    .clientSid(requestWrapper.getKeyClientApi()!=null?requestWrapper.getKeyClientApi().getClientSid():null)
                                    .publicKey(requestWrapper.getKeyClientApi()!= null?requestWrapper.getKeyClientApi().getPublicKeyAlias():requestWrapper.getUrlComponents().getHeaderKey("Api-Key"))
                                    .apiUid(requestWrapper.getKeyClientApi()!=null?requestWrapper.getKeyClientApi().getApi().getSerialID():null)
                                    .baseUrl(requestWrapper.getUrlComponents().getBaseUrl())
                                    .endPoint(requestWrapper.getUrlComponents().getEndPoint())
                                    .method(requestWrapper.getUrlComponents().getMethod())
                                    .ip(requestWrapper.getUrlComponents().getIp())
                                    .userAgent(requestWrapper.getUrlComponents().getHeaderKey("user-agent"))
                                    .time(Instant.now().toEpochMilli())
                                    .responseType(responseWrapper.getResponseType())
                                    .statusCode(responseWrapper.getResponseEntity().getStatusCodeValue())
                                    .DoBR(responseWrapper.getExecutionTime())
                            .build())
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        super.execute(requestWrapper, responseWrapper);
    }
}
