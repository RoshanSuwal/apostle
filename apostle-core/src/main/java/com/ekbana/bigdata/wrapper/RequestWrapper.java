package com.ekbana.bigdata.wrapper;

import com.ekbana.bigdata.entity.api.Api;
import com.ekbana.bigdata.entity.emails.Email;
import com.ekbana.bigdata.entity.ip.RemoteIP;
import com.ekbana.bigdata.entity.notification.Notification;
import com.ekbana.bigdata.entity.publickey.KeyClientApi;
import com.ekbana.bigdata.entity.publickey.PublicKeyAlias;
import com.ekbana.bigdata.helpers.UrlComponents;
import lombok.*;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RequestWrapper {
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    // KeyClientApi - Api, request-addons, mock-response, developer-webhook
    // PersonalPackage - personal packages, client-webhook
    private UrlComponents urlComponents;
    private Api api;

    private PublicKeyAlias publicKeyAlias;
    private KeyClientApi keyClientApi;
    private RemoteIP remoteIP;
    private Long startTime;

    @Builder.Default
    private Map<String,Object> metricsMapper=new HashMap<>();

    @Builder.Default
    private JSONObject metrics=new JSONObject();
    @Builder.Default
    private List<Email> emails=new ArrayList<>();
    @Builder.Default
    private List<Notification> notifications=new ArrayList<>();

    public void addEmail(Email email){
        emails.add(email);
    }

    public void addNotification(Notification notification){
        notifications.add(notification);
    }

    public void putInMetrics(String key,Object value){
        metrics.put(key,value);
        metricsMapper.put(key,value);
    }

}
