package com.ekbana.bigdata;

import com.ekbana.bigdata.annotation.*;
import com.ekbana.bigdata.configuration.ApiHandler;
import com.ekbana.bigdata.configuration.ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@SpringBootApplication
public class ApostleApp implements ApplicationRunner {

    private static Logger logger= LoggerFactory.getLogger(ApostleApp.class);

    private final ApplicationContext applicationContext;
    private final ApplicationConfiguration applicationConfiguration;
    private final ApiHandler apiHandler;

    @Autowired
    public ApostleApp(ApplicationContext applicationContext, ApplicationConfiguration applicationConfiguration, ApiHandler apiHandler) {
        this.applicationContext = applicationContext;
        this.applicationConfiguration = applicationConfiguration;
        this.apiHandler = apiHandler;
    }


    public static void main(String[] args) {
        SpringApplication.run(ApostleApp.class,args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Plugin.class);

//        applicationContext.getBeansWithAnnotation(Plugin.class).values().forEach(plugin-> ((com.ekbana.bigdata.plugin.Plugin)plugin).print());
//        beansWithAnnotation.forEach((key,value)->logger.info("{} {}",key,value));

        logger.info("Loaded Policies");
        applicationContext.getBeansWithAnnotation(Policy.class)
                .forEach((key,value)->logger.info("[{}] {} : {}","policy",key,value));

        logger.info("Loaded validators");
        applicationContext.getBeansWithAnnotation(Validator.class)
                .forEach((key,value)->logger.info("[{}] {} : {}","Validator",key,value));

        logger.info("Loaded Response Services");
        applicationContext.getBeansWithAnnotation(ResponseService.class)
                        .forEach((key,value)->logger.info("[{}] {} : {}","Response",key,value));

        logger.info("Loaded Post Services");
        applicationContext.getBeansWithAnnotation(PostService.class)
                .forEach((key,value)->logger.info("[{}] {} : {}","Post Service",key,value));

        logger.info("Loaded Notification Factories");
        applicationContext.getBeansWithAnnotation(NotificationFactory.class)
                .forEach((key,value)->logger.info("[{}] {} : {}","Notification",key,value));

    }
}
