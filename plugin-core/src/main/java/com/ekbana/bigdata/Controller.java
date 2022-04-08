package com.ekbana.bigdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class Controller {

    AntPathMatcher antPathMatcher=new AntPathMatcher();

    @GetMapping("/hello/{id}")
    public Map<String,String > hello(HttpServletRequest request, HttpServletResponse response, @PathVariable String id){
        System.out.println(request.getHttpServletMapping().getPattern());
        System.out.println(request.getHttpServletMapping().getMatchValue());
        System.out.println(request.getHttpServletMapping());
        System.out.println(id);
        Map<String, String> stringStringMap = antPathMatcher.extractUriTemplateVariables("/hello/{id}", request.getRequestURI());
        return stringStringMap;
    }

    @GetMapping("/select/{id}/document/{doc_id}")
    public ResponseEntity<?> select(){
        return ResponseEntity.ok("this is dummy response");
    }
}
