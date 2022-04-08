package com.ekbana.bigdata.inbound.notification;

import com.ekbana.bigdata.entity.notification.Notification;
import com.ekbana.bigdata.notification.NotificationFactory;
import com.ekbana.bigdata.notification.Notifier;
import com.ekbana.bigdata.post.PostService;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@com.ekbana.bigdata.annotation.PostService(value = "notification service")
public class NotificationService extends PostService {

    private final ApplicationContext applicationContext;

    @Autowired
    public NotificationService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void execute(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        // get the notifier i.e. mattermost, discord, slack
        // obtain the type from api and key-client-api
        // send notification according to notification type
        // get factory from notification factory
        if (requestWrapper.getNotifications().size()>0) {
            try {
                NotificationFactory notificationFactory = applicationContext.getBean("", NotificationFactory.class);
                Notifier notifier = notificationFactory.build();
                requestWrapper.getNotifications().stream().peek(notification -> log.info("[{}] {}", "Notification", notification.toString()))
                        .forEach(notification -> notifier.sendNotification(notification));
            } catch (Exception e) {
                log.error("[{}] {}", "Notification Service" + e.getMessage());
            }
        }
    }
}
