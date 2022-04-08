package com.ekbana.bigdata.built_in_plugins;

import com.ekbana.bigdata.plugin_api.Plugin;
import com.ekbana.bigdata.plugin_api.factory.NotificationFactory;

import java.util.Arrays;
import java.util.List;

public class BuiltInPlugins implements Plugin {
    @Override
    public String identify() {
        return "Built in plugin";
    }

    @Override
    public List<NotificationFactory> notificationFactories() {
        return Arrays.asList(new NTestFactory());
    }
}
