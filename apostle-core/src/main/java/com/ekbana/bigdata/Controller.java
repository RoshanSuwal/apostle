package com.ekbana.bigdata;

import com.ekbana.bigdata.configuration.ApiHandler;
import com.ekbana.bigdata.entity.api.Api;
import com.ekbana.bigdata.entity.ip.RemoteIP;
import com.ekbana.bigdata.helpers.UrlComponents;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@RestController
public class Controller {

    private static final Logger LOGGER= LoggerFactory.getLogger(Controller.class);
    @Autowired
    ApiHandler apiHandler;

    @CrossOrigin(originPatterns = "*")
    @RequestMapping("/**")
    public ResponseEntity<?> get(HttpServletRequest request, HttpServletResponse response){
        LOGGER.info("\n");
        LOGGER.info("[NEW REQUESTS] Accepted request for [" + request.getRequestURL() + "?" + request.getQueryString() + "]");
        UrlComponents urlComponents = new UrlComponents(request);
        RequestWrapper requestWrapper = RequestWrapper.builder()
                .httpServletRequest(request)
                .httpServletResponse(response)
                .urlComponents(urlComponents)
                .remoteIP(new RemoteIP(urlComponents.getIp()))
                .api(new Api(urlComponents.getBaseUrl(),urlComponents.getEndPoint(),urlComponents.getMethod()))
                .startTime(Instant.now().getEpochSecond())
                .build();

        return apiHandler.execute(requestWrapper);
    }

//    @GetMapping("/select/{id}/document/{doc_id}")
//    public ResponseEntity<?> select(@PathVariable("id") String id,@PathVariable("doc_id") String docId){
//        return ResponseEntity.ok("this is dummy response : "+id+" doc_id : "+docId);
//    }
}
