package com.ekbana.bigdata.entity.emails;

import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Email {
    String key;
    String to;
    String subject;
    String remark;
    int type;
}
