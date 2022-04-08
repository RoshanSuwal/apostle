package com.ekbana.bigdata.plugin_api;

import com.ekbana.bigdata.plugin_api.factory.NotificationFactory;
import com.ekbana.bigdata.plugin_api.factory.PluginFactory;
import com.ekbana.bigdata.plugin_api.factory.ValidatorFactory;

import java.util.Collections;
import java.util.List;

public interface Plugin {

    String identify();
    default List<Object> mvcControllers(){
        return Collections.emptyList();
    }

    default List<PluginFactory> pluginFactories(){
        return Collections.emptyList();
    }

    default List<NotificationFactory> notificationFactories(){return Collections.emptyList();}

    default List<ValidatorFactory> validatorFactories(){
        return Collections.emptyList();
    }

}
