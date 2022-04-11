package com.ekbana.bigdata.mattermost;

import com.ekbana.bigdata.entity.notification.Notification;
import com.ekbana.bigdata.notification.Notifier;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class MattermostWebhook extends Notifier {
    private final String webhook_url;
    private final String channel;

    public MattermostWebhook() {
        this.webhook_url="";
        this.channel="";
    }

    @Override
    public void sendNotification(Notification notification) {
        WebClient.create(webhook_url)
                .post()
                .body(BodyInserters.fromValue(notification))
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),error-> Mono.empty())
                .bodyToMono(String.class)
                .block();
    }
}
