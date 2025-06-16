package com.smile.review.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String authorization = attributes.getRequest().getHeader("Authorization");
                System.out.println(">>> Feign Authorization Header : " + authorization); // 로그로 꼭 확인!
                if (authorization != null) {
                    requestTemplate.header("Authorization", authorization);
                }
            }
        };
    }

}
