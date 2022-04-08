package com.ekbana.bigdata;

import com.ekbana.bigdata.plugin_api.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class MattermostNotification implements Notification {
    @Override
    public void configure() {

    }

    @Override
    public void send() {
        System.out.println("from mattermost notification");
    }
}
