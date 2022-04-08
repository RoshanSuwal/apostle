package com.ekbana.bigdata;

import com.ekbana.bigdata.plugin_api.Plugin;

public class TestPlug implements Plugin {
    @Override
    public String identify() {
        return "test";
    }
}
