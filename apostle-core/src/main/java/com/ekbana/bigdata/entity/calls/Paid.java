package com.ekbana.bigdata.entity.calls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paid implements Serializable {
    private String uuid ;
    private Long used_calls;
    private Long agreed_calls;
}
