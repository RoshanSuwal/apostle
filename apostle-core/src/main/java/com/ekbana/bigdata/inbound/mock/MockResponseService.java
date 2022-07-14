package com.ekbana.bigdata.inbound.mock;

import com.ekbana.bigdata.entity.response.Mock;
import com.ekbana.bigdata.response.ResponseService;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@com.ekbana.bigdata.annotation.ResponseService(value = "mock response")
public class MockResponseService extends ResponseService {
    public MockResponseService() {
        super("MOCK");
    }

    @Override
    protected ResponseEntity responseEntity(RequestWrapper request) {
        if (request.getKeyClientApi().getApi().getMock() != null)
            if (request.getKeyClientApi().getApi().getMock().getStatus())if (request.getKeyClientApi().getApi().getMock() != null)
                if (request.getKeyClientApi().getApi().getMock().getStatus()) {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Mock mock = request.getKeyClientApi().getApi().getMock();
                    try{
                        JsonNode jsonNode = objectMapper.readTree(mock.getResponseHeaders());
                        Iterator<Map.Entry<String, JsonNode>> fields = Objects.requireNonNull(jsonNode).fields();
                        while (fields.hasNext()) {
                            Map.Entry<String, JsonNode> field = fields.next();
                            httpHeaders.set(field.getKey(), field.getValue().asText());
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return ResponseEntity
                            .status(Integer.parseInt(mock.getResponseCode()))
                            .headers(httpHeaders)
                            .contentType(MediaType.parseMediaType(mock.getContentType()))
                            .body(mock.getResponseBody());
                }
        return null;
    }
}
