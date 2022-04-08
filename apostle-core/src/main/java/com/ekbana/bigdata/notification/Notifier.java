package com.ekbana.bigdata.notification;

import com.ekbana.bigdata.entity.notification.Notification;

public abstract class Notifier {
    public abstract void sendNotification(Notification notification);
}
