package com.ekbana.bigdata.policy;

import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Policy {

    public final static Logger log= LoggerFactory.getLogger(Policy.class);
    protected Policy nextPostPolicy=null;
    protected Policy nextPrePolicy=null;

    public Policy nextPrePolicy(Policy nextPolicy){
        this.nextPrePolicy=nextPolicy;
        return nextPolicy;
    }

    public Policy nextPostPolicy(Policy nextPolicy){
        this.nextPostPolicy=nextPolicy;
        return nextPolicy;
    }
    protected void post(RequestWrapper requestWrapper, ResponseWrapper responseWrapper){}
    protected void pre(RequestWrapper requestWrapper){}

    public RequestWrapper preExecute(RequestWrapper requestWrapper){
        log.info("[{}] {}","pre-policy",this.getClass().getSimpleName());
        pre(requestWrapper);
        return this.nextPrePolicy==null? requestWrapper:this.nextPrePolicy.preExecute(requestWrapper);
    }

    public void postExecute(RequestWrapper requestWrapper,ResponseWrapper responseWrapper){
        log.info("[{}] {}","post-policy",this.getClass().getSimpleName());
        post(requestWrapper,responseWrapper);
        if (this.nextPostPolicy!=null) this.nextPostPolicy.postExecute(requestWrapper,responseWrapper);

    }
}
