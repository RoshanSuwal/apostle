package com.ekbana.bigdata;

import com.ekbana.bigdata.plugin_api.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.util.Map;

@SpringBootApplication
public class App implements ApplicationRunner {

    private static final Logger logger= LoggerFactory.getLogger(App.class);

    @Bean
    public PluginLoader pluginLoader(){
        PluginLoader pluginLoader=new PluginLoader(new File("plugins"));
//        pluginLoader.loadInBuiltPlugins(Arrays.asList(new InbuiltPlugins()));
        pluginLoader.loadPlugins();
        return pluginLoader;
    }
    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(App.class,args);
    }

    @Autowired ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

//        Notification testValidator = applicationContext.getBean("testNotification", Notification.class);
//
//        System.out.println();
//        System.out.println();
//        System.out.println(testValidator.getClass().getSimpleName());
//        testValidator.send();

        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        requestMappingHandlerMapping.getHandlerMethods().forEach((key,value)-> logger.info("{} {}", key, value));

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Service.class);
        beansWithAnnotation.forEach((key,value)->logger.info("{} {}",key,value));
        for (String s : applicationContext.getBeanNamesForType(Notification.class)) {
            logger.info("Notification : [{}]",s);
        }
    }
}
