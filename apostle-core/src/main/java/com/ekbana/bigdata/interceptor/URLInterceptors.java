package com.ekbana.bigdata.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class URLInterceptors {

    /**
     * intercepts the Url
     *
     * @param urll  String represents the actual url without query params
     * @param queryStringFromRequest    String represents the query from the sent request
     * @param queryStringFromCMS    String represents query to be added to given Url
     *
     * @return String  represents the final Url
     * */
    @SneakyThrows
    public String getInterceptedUrl(String urll, String queryStringFromRequest, String queryStringFromCMS) {
        String finalUrl="";
        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse(urll)).newBuilder();
        ObjectMapper mapper = new ObjectMapper();
        if (queryStringFromCMS != null && queryStringFromCMS.length()>0) {
            JsonNode hNode = mapper.readTree(queryStringFromCMS);
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = hNode.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                url.addQueryParameter(field.getKey(), field.getValue().asText());
            }
            if (queryStringFromRequest != null) {
                finalUrl = url.build().uri().toString() + "&" + queryStringFromRequest;
            }else{
                finalUrl = url.build().uri().toString();
            }
        }else{
            finalUrl = url.build().uri()+"?"+queryStringFromRequest;
        }

        return finalUrl;

    }

}
