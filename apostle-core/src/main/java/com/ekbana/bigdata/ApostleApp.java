package com.ekbana.bigdata;

import com.ekbana.bigdata.annotation.NotificationFactory;
import com.ekbana.bigdata.annotation.Plugin;
import com.ekbana.bigdata.annotation.Policy;
import com.ekbana.bigdata.annotation.Validator;
import com.ekbana.bigdata.plugin.CoreValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@SpringBootApplication
public class ApostleApp implements ApplicationRunner {

    private static Logger logger= LoggerFactory.getLogger(ApostleApp.class);
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ConfigurableBeanFactory configurableBeanFactory;

    public static void main(String[] args) {
        SpringApplication.run(ApostleApp.class,args);

//        AntPathMatcher antPathMatcher = new AntPathMatcher();
//        String pattern = "/hello/{}/{}/{id}";
//        Map<String, String> stringStringMap = antPathMatcher.extractUriTemplateVariables(pattern.replace("{}","{_pv}"), "/hello/abc/p/123");
//        System.out.println(stringStringMap);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Plugin.class);

        applicationContext.getBeansWithAnnotation(Plugin.class).values().forEach(plugin-> ((com.ekbana.bigdata.plugin.Plugin)plugin).print());
        beansWithAnnotation.forEach((key,value)->logger.info("{} {}",key,value));

        logger.info("Policies");
        applicationContext.getBeansWithAnnotation(Policy.class)
                .forEach((key,value)->logger.info("{} {}",key,value));

        logger.info("validators");
        applicationContext.getBeansWithAnnotation(Validator.class)
                .forEach((key,value)->logger.info("{} {}",key,value));

        logger.info("Notification Factories");
        applicationContext.getBeansWithAnnotation(NotificationFactory.class)
                .forEach((key,value)->logger.info("{} {}",key,value));
    }
}
