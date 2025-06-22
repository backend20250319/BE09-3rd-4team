package com.smile.recommendservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@RequiredArgsConstructor
public class FeignAuthConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                String userId = authentication.getPrincipal().toString(); // String으로 캐스팅
                String role = authentication.getAuthorities().stream()
                        .map(Object::toString)
                        .findFirst()
                        .orElse("USER");

                template.header("X-User-Id", userId);
                template.header("X-User-Role", role);
                System.out.println("✅ Feign에 헤더 추가: " + userId + ", " + role);
            } else {
                System.out.println("❌ 인증 정보 없음");
            }
        };
    }

}
