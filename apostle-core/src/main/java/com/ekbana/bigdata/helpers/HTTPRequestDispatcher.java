package com.ekbana.bigdata.helpers;

import com.ekbana.bigdata.entity.api.Api;
import com.ekbana.bigdata.interceptor.HeaderInterceptor;
import com.ekbana.bigdata.interceptor.URLInterceptors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class HTTPRequestDispatcher {

    private OkHttpClient okHttp;
    private Request request;
    private String url;
    private String queryParameters;
    private String headers;
    private HashMap<String, String> replaceHeader;

    public Response sendGetRequest() throws IOException {
        OkHttpClient client = this.getOkHttpClient();

        Request rq = new Request.Builder()
                .url(this.url)
                .build();
        return client.newCall(rq).execute();
    }

    public Response sendPostRequest(RequestBody request) throws IOException {
        OkHttpClient client = this.getOkHttpClient();

        Request rq = new Request.Builder()
                .url(this.url)
                //.post(RequestBody.create(MediaType.parse(request.getContentType()), String.copyValueOf(buffer)))
                .post(request)
                .build();
        return client.newCall(rq).execute();
    }

    private OkHttpClient getOkHttpClient() {
        if (this.headers == null) {
            return this.okHttp
                    .newBuilder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(new HeaderInterceptor(null, this.replaceHeader))
                    .build();
        } else {
            return this.okHttp
                    .newBuilder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(new HeaderInterceptor(this.headers, this.replaceHeader))
                    .build();
        }
    }

    public Response sendEmail(String to, String subject, String remark,int type) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("to", to).put("subject", subject).put("remark", remark).put("type",type);
        OkHttpClient client = this.getOkHttpClient();
        Response response = null;
        try {
            Request rq = new Request.Builder()
                    .url(this.url)
                    .post(RequestBody.create(objectMapper.writeValueAsString(jsonNode),
                            MediaType.parse("application/json; charset=utf-8")))
                    .build();

            response = client.newCall(rq).execute();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return response;
    }

    public HTTPRequestDispatcher setRedirectToUrl(Api api, String queryStringFromRequest) {
        StringBuilder stringBuilder = new StringBuilder(api.getProtocol().toLowerCase())
                .append("://")
                .append(api.getRedirectTo())
                .append("/")
                .append(api.getParameters());
        this.url = new URLInterceptors()
                .getInterceptedUrl(stringBuilder.toString(), queryStringFromRequest, this.queryParameters);

        log.info("[BACKEND] sending request to "+this.url);
        return this;
    }

}
