package com.ekbana.bigdata;

import com.ekbana.bigdata.configuration.ApiHandler;
import com.ekbana.bigdata.entity.api.Api;
import com.ekbana.bigdata.entity.ip.RemoteIP;
import com.ekbana.bigdata.helpers.UrlComponents;
import com.ekbana.bigdata.wrapper.RequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@RestController
public class Controller {

    @Autowired
    ApiHandler apiHandler;

    @RequestMapping("/**")
    public ResponseEntity<?> get(HttpServletRequest request, HttpServletResponse response){

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
