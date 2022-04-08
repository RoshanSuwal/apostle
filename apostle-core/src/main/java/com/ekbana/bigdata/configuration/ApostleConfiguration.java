package com.ekbana.bigdata.configuration;

import com.ekbana.bigdata.policy.Policy;
import com.ekbana.bigdata.post.PostService;
import com.ekbana.bigdata.response.ResponseService;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Arrays;

@Configuration
public class ApostleConfiguration {

    // define the rules here
    // pre-policy rules
    // response-policy rules
    // post-policy rules

    @Autowired
    private ApplicationContext applicationContext;

    @Bean("pre policy")
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

        for (String prePolicy : prePolicies) {
            try {
                policy = policy == null ?
                        applicationContext.getBean(prePolicy, Policy.class) :
                        policy.nextPolicy(applicationContext.getBean(prePolicy, Policy.class));
                if (startingPolicy == null) startingPolicy = policy;
            } catch (BeanNotOfRequiredTypeException exception) {
                System.out.println(exception.toString());
            } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
                System.out.println(noSuchBeanDefinitionException.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Policy api_validation_policy = applicationContext.getBean("api validation policy", Policy.class);
//        Policy public_key_policy = applicationContext.getBean("public key policy", Policy.class);
//        Policy ip_policy = applicationContext.getBean("ip policy", Policy.class);
//        Policy package_validation_policy = applicationContext.getBean("package validation policy", Policy.class);
//        Policy quota_policy = applicationContext.getBean("quota policy", Policy.class);
//        Policy rate_limit_policy = applicationContext.getBean("rate limit policy", Policy.class);
//
//        ip_policy
//                .nextPolicy(public_key_policy)
//                .nextPolicy(api_validation_policy)
//                .nextPolicy(package_validation_policy)
//                .nextPolicy(quota_policy)
//                .nextPolicy(rate_limit_policy);
//
//        return ip_policy;
        return startingPolicy;
    }

    @Bean("post policy")
    public Policy postPolicy() {
        String[] postPolicies = new String[]{
                "quota policy",
                "rate limit policy"
        };

        Policy policy = null;
        Policy startingPolicy = null;

        for (String prePolicy : postPolicies) {
            try {
                policy = policy == null ?
                        applicationContext.getBean(prePolicy, Policy.class) :
                        policy.nextPolicy(applicationContext.getBean(prePolicy, Policy.class));
                if (startingPolicy == null) startingPolicy = policy;
            } catch (BeanNotOfRequiredTypeException exception) {
                System.out.println(exception.toString());
            } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
                System.out.println(noSuchBeanDefinitionException.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return startingPolicy;
    }

    @Bean(value = "response")
    public ResponseService responseService() {
        String[] responseServices = new String[]{
                "mock response",
                "cache response",
                "backend response"
        };

        ResponseService startingResponseService = null;
        ResponseService responseService = null;

        for (String serviceName : responseServices) {
            try {
                responseService = responseService == null ?
                        applicationContext.getBean(serviceName, ResponseService.class)
                        : responseService.nextService(applicationContext.getBean(serviceName, ResponseService.class));
                if (startingResponseService == null) startingResponseService = responseService;
            }catch (BeanNotOfRequiredTypeException e){
                System.out.println(e.toString());
            }catch (NoSuchBeanDefinitionException e){
                e.toString();
            }
        }
        return startingResponseService;
    }

    @Bean(value = "post service")
    public PostService postService(){
        String[] postServicesName=new String[]{
                "metrics service",
                "email service",
                "notification service"
        };

        PostService postService=null;
        PostService startingPostService=null;
        for (String postServiceName : postServicesName) {
            try {
                postService = postService == null ?
                        applicationContext.getBean(postServiceName, PostService.class)
                        : postService.nextService(applicationContext.getBean(postServiceName, PostService.class));
                if (startingPostService == null) startingPostService = postService;
            }catch (BeanNotOfRequiredTypeException e){
                System.out.println(e.toString());
            }catch (NoSuchBeanDefinitionException e){
                e.toString();
            }
        }
        return startingPostService;
    }
    @Bean
    @DependsOn({"pre policy", "post policy", "response"})
    public ApiHandler apiHandler() {
        return ApiHandler.builder()
                .prePolicy(prePolicy())
                .responseService(responseService())
                .postPolicy(postPolicy())
                .postService(postService())
                .build();
    }

}
