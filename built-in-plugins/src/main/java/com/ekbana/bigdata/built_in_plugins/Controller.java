package com.ekbana.bigdata.built_in_plugins;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @RequestMapping("/hello")
    public String hello(){
        return "hello from built in plugins";
    }
}
