package com.ekbana.bigdata;

import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

@Service
public class TestService {

    public static void main(String[] args) {
        AntPathMatcher antPathMatcher=new AntPathMatcher();
        System.out.println(antPathMatcher.match("/hello/{name}/address","/hello/ab/address"));
        System.out.println(antPathMatcher.match("/hello/{name}/address","/hello/123/address"));
        System.out.println(antPathMatcher.match("/hello/{name}/address","/hello//address"));
        System.out.println(antPathMatcher.match("/hello/{name}/address","/hello/address/address"));
        System.out.println(antPathMatcher.match("/hello/{name}/address","/hellos/ab/address"));
    }
}
