package com.ekbana.bigdata.entity.notification;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
@ToString
public class Notification {
    private boolean urgent;
    private String message;
}
