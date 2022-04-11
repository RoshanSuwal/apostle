package com.ekbana.bigdata.mattermost;

import com.ekbana.bigdata.notification.NotificationFactory;
import com.ekbana.bigdata.notification.Notifier;

@com.ekbana.bigdata.annotation.NotificationFactory(value = "mattermost webhook")
public class MattermostWebhookFactory extends NotificationFactory {

    @Override
    public Notifier build() {
        return new MattermostWebhook();
    }
}
