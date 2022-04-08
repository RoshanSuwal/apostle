package com.ekbana.bigdata.plugin_api.factory;

import com.ekbana.bigdata.plugin_api.Message;
import com.ekbana.bigdata.plugin_api.model.Notification;

public interface NotificationFactory {
    String identity();
    Notification build(Message message);
}
