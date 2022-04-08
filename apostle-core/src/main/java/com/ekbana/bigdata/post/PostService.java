package com.ekbana.bigdata.post;

import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostService {
    protected final static Logger log= LoggerFactory.getLogger(Policy.class);
    protected PostService nextService=null;

    public PostService nextService(PostService nextService){
        this.nextService=nextService;
        return nextService;
    }
    protected void execute(RequestWrapper requestWrapper, ResponseWrapper responseWrapper){}

    public void postServiceExecute(RequestWrapper requestWrapper,ResponseWrapper responseWrapper){
        log.info("[{}] {}","post service",this.getClass().getSimpleName());
        execute(requestWrapper,responseWrapper);
        if (this.nextService!=null) this.nextService.postServiceExecute(requestWrapper,responseWrapper);

    }
}
