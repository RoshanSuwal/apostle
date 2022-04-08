package com.ekbana.bigdata.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HeaderInterceptor implements Interceptor {
    private String requestAddons;
    private HashMap<String , String> headerPH;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder b = request.newBuilder();
        ObjectMapper mapper = new ObjectMapper();

        if (!(this.requestAddons == null)) {
            try {
                JsonNode hNode = mapper.readTree(this.requestAddons);
                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = hNode.fields();
                while (fieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> field = fieldsIterator.next();
                    b.addHeader(field.getKey(),field.getValue().asText());
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if(!(this.headerPH == null)){
            for(String key:this.headerPH.keySet()){
                b.header(key,this.headerPH.get(key));
            }
        }

        return chain.proceed(b.build());
    }

    public HeaderInterceptor(String abc,HashMap<String, String> headerPH){
        this.requestAddons = abc;
        this.headerPH = headerPH;
    }

}
