package com.ekbana.bigdata.inbound.ip;

import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;

@com.ekbana.bigdata.annotation.Policy(value = "ip policy")
public class IpValidationPolicy extends Policy {

    private final IpService ipService;

    @Autowired
    public IpValidationPolicy(IpService ipService) {
        this.ipService = ipService;
    }

    @Override
    protected void pre(RequestWrapper requestWrapper) {
        // ip blocked
        if (ipService.isBlackListed(requestWrapper.getRemoteIP().getIp_addr()))
            throw new IpException(" Ip has been blocked",requestWrapper);
    }

    @Override
    protected void post(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) {
        super.post(requestWrapper, responseWrapper);

        // TODO :
        // executed on the basis of response status
        // 1. logic for marking the ip as marked ip
        // 2. logic for blocking the ip
    }
}
