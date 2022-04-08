package com.ekbana.bigdata.built_in_plugins;

import com.ekbana.bigdata.plugin_api.Message;
import com.ekbana.bigdata.plugin_api.factory.NotificationFactory;
import com.ekbana.bigdata.plugin_api.model.Notification;

public class NTestFactory implements NotificationFactory {
    @Override
    public String identity() {
        return "test notification";
    }

    @Override
    public Notification build(Message message) {

        System.out.println(message.getMessage());
        return null;
    }
}
