package com.ekbana.bigdata.built_in_plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestValidator {
    private final TestNotification testNotification;

    @Autowired
    public TestValidator(TestNotification testNotification) {
        this.testNotification=testNotification;
    }
}
