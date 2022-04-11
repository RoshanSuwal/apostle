package com.ekbana.bigdata.notification;

import org.json.JSONObject;

public abstract class NotificationFactory {
    public abstract Notifier build(JSONObject jsonObject);
}
