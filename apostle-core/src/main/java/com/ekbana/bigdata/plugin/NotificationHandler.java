package com.ekbana.bigdata.plugin;

import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import com.ekbana.bigdata.annotation.Policy;

//@Policy(value = "notification service")
public class NotificationHandler extends com.ekbana.bigdata.policy.Policy {

    @Override
    protected void post(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        super.post(requestWrapper, responseWrapper);
    }
}
