package com.ekbana.bigdata.built_in_plugins;

import com.ekbana.bigdata.plugin_api.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class TestNotification implements Notification {
    @Autowired
    ApplicationContext applicationContext;
    @Override
    public void configure() {

    }

    @Override
    public void send() {
        System.out.println("this if from test notification");
        System.out.println(applicationContext.getBeanDefinitionNames().length);
        Object pluginLoader = applicationContext.getBean("pluginLoader");
        System.out.println(pluginLoader.getClass());

        Notification mattermostNotification = applicationContext.getBean("mattermostNotification", Notification.class);
        mattermostNotification.send();
    }
}
