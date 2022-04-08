package com.ekbana.bigdata.entity.calls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grace implements Serializable {
    private String uuid;
    private Long used_grace_calls;
    private Long agreed_grace_calls;
}
