package com.ekbana.bigdata.entity.publickey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpiredPublicKey implements Serializable {

    private String publicKey;
    private Long expired_on;
    private String reason;

}
