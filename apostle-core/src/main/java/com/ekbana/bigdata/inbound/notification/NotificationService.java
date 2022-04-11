package com.ekbana.bigdata.inbound.notification;

import com.ekbana.bigdata.entity.webhook.ApiWebhook;
import com.ekbana.bigdata.entity.webhook.PersonalWebhook;
import com.ekbana.bigdata.entity.webhook.Webhook;
import com.ekbana.bigdata.notification.NotificationFactory;
import com.ekbana.bigdata.post.PostService;
import com.ekbana.bigdata.repository.jpa.ApiWebhooksRepository;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;

@com.ekbana.bigdata.annotation.PostService(value = "notification service")
public class NotificationService extends PostService {

    private final ApplicationContext applicationContext;
    private final ApiWebhooksRepository personalWebhooksRepository;

    @Autowired
    public NotificationService(ApplicationContext applicationContext, ApiWebhooksRepository personalWebhooksRepository) {
        this.applicationContext = applicationContext;
        this.personalWebhooksRepository = personalWebhooksRepository;
    }

    @Override
    protected void execute(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        // get the notifier i.e. mattermost, discord, slack
        // obtain the type from api and key-client-api
        // send notification according to notification type
        // get factory from notification factory
        if (requestWrapper.getNotifications().size() > 0) {
            try {
                List<ApiWebhook> apiWebhooks = personalWebhooksRepository.findByApiId(requestWrapper.getKeyClientApi().getApiId());

                apiWebhooks.stream()
                        .map(apiWebhook -> apiWebhook.getWebhook())
                        .map(personalWebhook -> personalWebhook.getFunnel())
                        .map(funnel -> new JSONObject(funnel))
                        .map(fun -> {
                            try {
                                return applicationContext.getBean(fun.getString("type"), NotificationFactory.class)
                                        .build(fun);
                            } catch (Exception e) {
                                log.error("[{}] {}", "Notification Service" , e.getMessage());
                                return null;
                            }
                        }).filter(notifier -> notifier != null)
                        .forEach(notifier -> requestWrapper.getNotifications().stream()
                                .peek(notification -> log.info("[{}] {}", notifier.getClass().getSimpleName(), notification.toString()))
                                .forEach(notification -> {
                                    if (notification.isUrgent()) {
                                        notifier.sendNotification(notification);
                                    } else {
                                        // save notification to databases
                                    }
                                }));
            } catch (Exception e) {
                log.error("[{}] {}", "Notification Service" , e.getMessage());
            }
        }
    }
}
