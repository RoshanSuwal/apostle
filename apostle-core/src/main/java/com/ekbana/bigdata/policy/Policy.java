package com.ekbana.bigdata.policy;

import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Policy {

    private final static Logger log= LoggerFactory.getLogger(Policy.class);
    protected Policy nextPolicy=null;

    public Policy nextPolicy(Policy nextPolicy){
        this.nextPolicy=nextPolicy;
        return nextPolicy;
    }
    protected void post(RequestWrapper requestWrapper, ResponseWrapper responseWrapper){}
    protected void pre(RequestWrapper requestWrapper){}

    public RequestWrapper preExecute(RequestWrapper requestWrapper){
        log.info("[{}] {}","pre-policy",this.getClass().getSimpleName());
        pre(requestWrapper);
        return this.nextPolicy==null? requestWrapper:this.nextPolicy.preExecute(requestWrapper);
    }

    public void postExecute(RequestWrapper requestWrapper,ResponseWrapper responseWrapper){
        log.info("[{}] {}","post-policy",this.getClass().getSimpleName());
        post(requestWrapper,responseWrapper);
        if (this.nextPolicy!=null) this.nextPolicy.postExecute(requestWrapper,responseWrapper);

    }
}
