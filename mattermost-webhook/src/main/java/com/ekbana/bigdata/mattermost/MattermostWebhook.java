package com.ekbana.bigdata.mattermost;

import com.ekbana.bigdata.entity.notification.Notification;
import com.ekbana.bigdata.notification.Notifier;
import lombok.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MattermostWebhook extends Notifier {
    private String webhook_url;
    private String channel;

    @Override
    public void sendNotification(Notification notification) {
        WebClient.create(webhook_url)
                .post()
                .body(BodyInserters.fromValue(new Mattermost(getChannel(),notification.getMessage())))
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),error-> Mono.empty())
                .bodyToMono(String.class)
                .block();
    }

    @Data
    @AllArgsConstructor @NoArgsConstructor
    private static class Mattermost{
        private String channel;
        private String text;
    }
}
