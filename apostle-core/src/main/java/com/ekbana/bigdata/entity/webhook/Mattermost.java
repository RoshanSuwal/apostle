package com.ekbana.bigdata.entity.webhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mattermost {
    private String text;
    @Builder.Default
    private String username = "saurav";
    @Builder.Default
    private String channel = "ams-notification";
}
