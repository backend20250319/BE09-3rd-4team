package com.smile.searchservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) REST API 는 CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 2) 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // GET 은 모두 허용
                        .requestMatchers(HttpMethod.GET, "/search/**").permitAll()
                        // POST/PUT/DELETE 는 ADMIN 역할만 허용
                        .requestMatchers(HttpMethod.POST, "/search/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/search/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/search/**").hasRole("ADMIN")
                        // 그 외 요청은 인증된 사용자만
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}