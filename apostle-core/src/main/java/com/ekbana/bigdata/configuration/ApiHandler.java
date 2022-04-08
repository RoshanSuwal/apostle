package com.ekbana.bigdata.configuration;

import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.post.PostService;
import com.ekbana.bigdata.response.ResponseService;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import com.ekbana.bigdata.wrapper.ResponseWrapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Builder
@Getter @Setter
public class ApiHandler {
    private final Policy prePolicy;
    private final Policy postPolicy;
    private final Policy errorPolicy;
    private final ResponseService responseService;
    private final PostService postService;

    public ResponseEntity<?> execute(RequestWrapper wrapper ){
        RequestWrapper requestWrapper = prePolicy.preExecute(wrapper);
        // todo response service
        ResponseWrapper responseWrapper=responseService.response(requestWrapper);
        postPolicy.postExecute(requestWrapper,responseWrapper);
        postService.postServiceExecute(requestWrapper,responseWrapper);
        return responseWrapper.getResponseEntity();
    }
}
