package com.ekbana.bigdata.inbound.ip;

import com.ekbana.bigdata.configuration.ApplicationConfiguration;
import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;

@com.ekbana.bigdata.annotation.Policy(value = "ip policy")
public class IpValidationPolicy extends Policy {

    private final IpService ipService;
    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public IpValidationPolicy(IpService ipService, ApplicationConfiguration applicationConfiguration) {
        this.ipService = ipService;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    protected void pre(RequestWrapper requestWrapper) {
        // ip blocked
        if (ipService.isBlackListed(requestWrapper.getRemoteIP().getIp_addr()))
            throw new IpException(applicationConfiguration.getIP_BLOCKED_MSG(),requestWrapper);
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
