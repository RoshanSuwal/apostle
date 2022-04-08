package com.ekbana.bigdata.entity.ip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@NoArgsConstructor
@Getter
@Setter
public class BlacklistIPCounter extends RemoteIP implements Serializable {
    public Integer counter;
    public BlacklistIPCounter(String ip_addr) {
        super(ip_addr);
    }
}
