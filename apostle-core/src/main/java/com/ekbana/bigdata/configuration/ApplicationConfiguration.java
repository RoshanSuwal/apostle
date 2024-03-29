package com.ekbana.bigdata.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;

//@PropertySource("file:///home/ek-desktop-26/workspace/bigdata/apostle/env_var.properties")
@PropertySource("file:///etc/ekmiddleware/properties/env_var.properties")
@Configuration
@Getter
@Setter
@ConfigurationProperties
public class ApplicationConfiguration {

    private String KEY_EXPIRATION_LOWER_WATERMARK_DAYS = "1";
    private String ALLOWED_CALLS_LOWER_WATERMARK = "1";
    private String GRACE_CALLS_LOWER_WATERMARK = "1";

    public int[] getKEY_EXPIRATION_LOWER_WATERMARK_DAYS() {
        return Arrays.stream(KEY_EXPIRATION_LOWER_WATERMARK_DAYS
                        .split(","))
                .mapToInt(s -> Integer.parseInt(s))
                .sorted()
                .toArray();
    }

    public int[] getALLOWED_CALLS_LOWER_WATERMARKS() {
        return Arrays.stream(ALLOWED_CALLS_LOWER_WATERMARK
                        .split(","))
                .mapToInt(s -> Integer.parseInt(s))
                .toArray();
    }

    public int[] getGRACE_CALLS_LOWER_WATERMARKS() {
        return Arrays.stream(GRACE_CALLS_LOWER_WATERMARK
                        .split(","))
                .mapToInt(s -> Integer.parseInt(s))
                .sorted()
                .toArray();
    }

    private String IP_BLOCKED_MSG;
    private int IP_BLOCKED_CODE;

    private String KEY_EXPIRATION_DATE_REACHED_MSG;
    private int KEY_EXPIRATION_DATE_REACHED_CODE;

    private String ALLOWED_CALLS_EXHAUSTED_MSG;
    private int ALLOWED_CALLS_EXHAUSTED_CODE;

    private String SUBSCRIPTION_ENDED_MSG;
    private int SUBSCRIPTION_ENDED_CODE;

    private String INVALID_KEY_MSG;
    private int INVALID_KEY_CODE;

    private String INACTIVE_API_MSG;
    private int INACTIVE_API_CODE;

    private String UNREGISTERED_API_MSG;
    private int UNREGISTERED_API_CODE;

    private String ILLEGAL_PUBLIC_KEY_MSG;

    private String EXPIRED_PUBLIC_KEY_MSG;

    private String INACTIVE_KEY_MSG;
    private int INACTIVE_KEY_CODE;


    /**
     * Policies
     */
    private String PRE_POLICIES = "ip policy,public key policy,api validation policy,package validation policy,quota policy,rate limit policy";
    private String POST_POLICIES = "quota policy,rate limit policy";
    private String RESPONSE_ORDER = "mock response,cache response,backend response";
    private String POST_SERVICES = "metrics service,email service, notification service";

    public String[] getPRE_POLICIES() {
        return Arrays.stream(PRE_POLICIES.split(",")).map(s -> s.trim()).toArray(String[]::new);
    }

    public String[] getPOST_POLICIES() {
        return Arrays.stream(POST_POLICIES.split(",")).map(s -> s.trim()).toArray(String[]::new);
    }

    public String[] getRESPONSE_ORDER() {
        return Arrays.stream(RESPONSE_ORDER.split(",")).map(s -> s.trim()).toArray(String[]::new);
    }

    public String[] getPOST_SERVICES() {
        return Arrays.stream(POST_SERVICES.split(",")).map(s -> s.trim()).toArray(String[]::new);
    }

    private String TIMEZONE_OFFSET = "+00:00";

    public String getTIMEZONE_OFFSET() {
        return TIMEZONE_OFFSET;
    }
}
