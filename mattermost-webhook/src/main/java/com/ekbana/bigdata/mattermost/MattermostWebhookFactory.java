package com.ekbana.bigdata.mattermost;

import com.ekbana.bigdata.notification.NotificationFactory;
import com.ekbana.bigdata.notification.Notifier;
import org.json.JSONObject;

@com.ekbana.bigdata.annotation.NotificationFactory(value = "mattermost")
public class MattermostWebhookFactory extends NotificationFactory {

    @Override
    public Notifier build(JSONObject jsonObject) {
        return MattermostWebhook.builder()
                .webhook_url(jsonObject.getString("webhook_url"))
                .channel(jsonObject.getString("channel"))
                .build();
    }
}
