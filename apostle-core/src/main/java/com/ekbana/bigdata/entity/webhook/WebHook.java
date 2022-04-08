package com.ekbana.bigdata.entity.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebHook {
    String webHookType;
    String webHookTemplate;
}
