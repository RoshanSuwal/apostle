package com.ekbana.bigdata.inbound.metric;

import lombok.*;

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

}
