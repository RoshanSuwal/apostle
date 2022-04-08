package com.ekbana.bigdata.plugin;

import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.annotation.Validator;

@Validator(value = "core validator")
public class CoreValidator extends Policy {

    @Override
    protected void pre(RequestWrapper requestWrapper) {
        super.pre(requestWrapper);
    }
}
