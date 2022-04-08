package com.ekbana.bigdata.entity.ip;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarkedIP extends RemoteIP implements Serializable {

    private Long blacklisted_on;
    private String reason;
    public MarkedIP(String ip_addr) {
        super(ip_addr);
    }
}
