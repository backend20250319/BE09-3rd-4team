package com.smile.recommendservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Gateway가 전달한 사용자 정보를 인증 정보로 설정
 */
@Component
@Slf4j
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String gender = request.getHeader("X-User-Gender");
        String ageStr = request.getHeader("X-User-Age");

        log.debug("인증 헤더 수신: userId={}, gender={}, age={}", userId, gender, ageStr);

        if (userId != null) {
            Integer age = null;
            try {
                if (ageStr != null) {
                    age = Integer.parseInt(ageStr);
                }
            } catch (NumberFormatException e) {
                log.warn("X-User-Age 헤더 값이 정수가 아닙니다: {}", ageStr);
            }

            Map<String, Object> principal = new HashMap<>();
            principal.put("userId", userId);
            principal.put("gender", gender);
            principal.put("age", age);

            PreAuthenticatedAuthenticationToken authentication =
                    new PreAuthenticatedAuthenticationToken(principal, null, null); // ← 권한 없이 생성

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
