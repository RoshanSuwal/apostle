package com.ekbana.bigdata.built_in_plugins;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestTestABC {

    @Bean
    public TestNotification testNotification(){
        return new TestNotification();
    }
}
