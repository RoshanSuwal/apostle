package com.ekbana.bigdata.plugin_api.factory;

public interface PluginFactory<T> {
    void factoryIdentity(String identity);
    T build();
}
