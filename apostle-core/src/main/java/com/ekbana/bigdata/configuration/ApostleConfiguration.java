package com.ekbana.bigdata.configuration;

import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.post.PostService;
import com.ekbana.bigdata.response.ResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ApostleConfiguration {

    // define the rules here
    // pre-policy rules
    // response-policy rules
    // post-policy rules

    private static final Logger log= LoggerFactory.getLogger(ApostleConfiguration.class);

    private final ApplicationContext applicationContext;
    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public ApostleConfiguration(ApplicationContext applicationContext, ApplicationConfiguration applicationConfiguration) {
        this.applicationContext = applicationContext;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Bean("pre policy order")
    public Policy prePolicy() {
        String[] prePolicies = new String[]{
                "ip policy",
                "public key policy",
                "plllmdmmd",
                "api validation policy",
                "package validation policy",
                "quota policy",
                "rate limit policy"
        };

        Policy policy = null;
        Policy startingPolicy = null;

        log.info("");
        log.info("{}","configuring pre-policy");
        for (String prePolicy : applicationConfiguration.getPRE_POLICIES()) {
            try {
                policy = policy == null ?
                        applicationContext.getBean(prePolicy, Policy.class) :
                        policy.nextPolicy(applicationContext.getBean(prePolicy, Policy.class));
                if (startingPolicy == null) startingPolicy = policy;
                log.info("[{}] {}","pre-policy",prePolicy);
            } catch (BeanNotOfRequiredTypeException exception) {
                log.error(exception.toString());
            } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
                log.error(noSuchBeanDefinitionException.toString());
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
        return startingPolicy;
    }

    @Bean("post policy order")
    public Policy postPolicy() {
        String[] postPolicies = new String[]{
                "quota policy",
                "rate limit policy"
        };

        Policy policy = null;
        Policy startingPolicy = null;

        log.info("");
        log.info("{}","configuring post-policy");
        for (String postPolicy : applicationConfiguration.getPOST_POLICIES()) {
            try {
                policy = policy == null ?
                        applicationContext.getBean(postPolicy, Policy.class) :
                        policy.nextPolicy(applicationContext.getBean(postPolicy, Policy.class));
                if (startingPolicy == null) startingPolicy = policy;
                log.info("[{}] {}","post-policy",postPolicy);
            } catch (BeanNotOfRequiredTypeException exception) {
                log.error(exception.toString());
            } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
                log.error(noSuchBeanDefinitionException.toString());
            } catch (Exception e) {
                log.error("",e);
            }
        }
        return startingPolicy;
    }

    @Bean(value = "response order")
    public ResponseService responseService() {
        String[] responseServices = new String[]{
                "mock response",
                "cache response",
                "backend response"
        };

        ResponseService startingResponseService = null;
        ResponseService responseService = null;

        log.info("");
        log.info("{}","configuring response service");
        for (String serviceName : applicationConfiguration.getRESPONSE_ORDER()) {
            try {
                responseService = responseService == null ?
                        applicationContext.getBean(serviceName, ResponseService.class)
                        : responseService.nextService(applicationContext.getBean(serviceName, ResponseService.class));
                if (startingResponseService == null) startingResponseService = responseService;
                log.info("[{}] {}","response service",serviceName);
            }catch (BeanNotOfRequiredTypeException e){
                log.error("",e);
            }catch (NoSuchBeanDefinitionException e){
                log.error("",e);
            }
        }
        return startingResponseService;
    }

    @Bean(value = "post service order")
    public PostService postService(){
        String[] postServicesName=new String[]{
                "metrics service",
                "email service",
                "notification service"
        };

        PostService postService=null;
        PostService startingPostService=null;

        log.info("");
        log.info("{}","configuring post service");

        for (String postServiceName : applicationConfiguration.getPOST_SERVICES()) {
            try {
                postService = postService == null ?
                        applicationContext.getBean(postServiceName, PostService.class)
                        : postService.nextService(applicationContext.getBean(postServiceName, PostService.class));
                if (startingPostService == null) startingPostService = postService;
                log.info("[{}] {}","post service",postServiceName);
            }catch (BeanNotOfRequiredTypeException e){
                log.error("",e);
            }catch (NoSuchBeanDefinitionException e){
                log.error("",e);
            }
        }
        return startingPostService;
    }
    @Bean
    @DependsOn({"pre policy order", "post policy order", "response order"})
    public ApiHandler apiHandler() {
        return ApiHandler.builder()
                .prePolicy(prePolicy())
                .responseService(responseService())
                .postPolicy(postPolicy())
                .postService(postService())
                .build();
    }

}
