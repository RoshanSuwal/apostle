package com.ekbana.bigdata.response;

import com.ekbana.bigdata.entity.notification.Notification;
import com.ekbana.bigdata.exception.BaseException;
import com.ekbana.bigdata.helpers.HTTPRequestDispatcher;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import okhttp3.OkHttpClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@com.ekbana.bigdata.annotation.ResponseService(value = "backend response")
public class BackendResponseService extends ResponseService {

    public BackendResponseService() {
        super("BACKEND");
    }

    @Override
    protected ResponseEntity<?> responseEntity(RequestWrapper requestWrapper) {
        HashMap<String, String> replaceHeader = requestWrapper.getUrlComponents().getHeaders();
        replaceHeader.remove("api-key");
        requestWrapper.getKeyClientApi().getApi().setParameters(requestWrapper.getUrlComponents().getEndPoint());
        try {
            if (requestWrapper.getUrlComponents().getMethod().equals(HttpMethod.GET.toString())) {
                okhttp3.Response response = HTTPRequestDispatcher.builder()
                        .okHttp(new OkHttpClient())
                        .headers(requestWrapper.getKeyClientApi().getApi().getRequestAddons() == null ? null : requestWrapper.getKeyClientApi().getApi().getRequestAddons().getRequestHeaders())
                        .replaceHeader(replaceHeader)
                        .queryParameters(requestWrapper.getKeyClientApi().getApi().getRequestAddons() == null ? null : requestWrapper.getKeyClientApi().getApi().getRequestAddons().getQueryParam())
                        .build()
                        .setRedirectToUrl(requestWrapper.getKeyClientApi().getApi(), requestWrapper.getHttpServletRequest().getQueryString())
                        .sendGetRequest();
                return ResponseEntity.status(response.code()).body(Objects.requireNonNull(response.body()).toString());
            } else if (requestWrapper.getUrlComponents().getMethod().equals(HttpMethod.POST.toString())) {
                okhttp3.Response response = HTTPRequestDispatcher.builder()
                        .okHttp(new OkHttpClient())
                        .headers(requestWrapper.getKeyClientApi().getApi().getRequestAddons() == null ? null : requestWrapper.getKeyClientApi().getApi().getRequestAddons().getRequestHeaders())
                        .replaceHeader(replaceHeader)
                        .queryParameters(requestWrapper.getKeyClientApi().getApi().getRequestAddons() == null ? null : requestWrapper.getKeyClientApi().getApi().getRequestAddons().getQueryParam())
                        .build()
                        .setRedirectToUrl(requestWrapper.getKeyClientApi().getApi(), requestWrapper.getHttpServletRequest().getQueryString())
                        .sendPostRequest(requestWrapper.getUrlComponents().getRequest());
                return ResponseEntity.status(response.code()).body(Objects.requireNonNull(response.body()).toString());
            } else {
                requestWrapper.addNotification(Notification.builder().urgent(true).message(requestWrapper.getUrlComponents().getMethod()+" service not available").build());
                throw new BaseException(requestWrapper.getUrlComponents().getMethod()+" service not available", HttpStatus.SERVICE_UNAVAILABLE, requestWrapper);
            }
        } catch (IOException e) {
            requestWrapper.addNotification(Notification.builder().urgent(true).message(e.getMessage()).build());
            throw new BaseException("Page Not Found", HttpStatus.SERVICE_UNAVAILABLE, requestWrapper);
        }
    }
}
