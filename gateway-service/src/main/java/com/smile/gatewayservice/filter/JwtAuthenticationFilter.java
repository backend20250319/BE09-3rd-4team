package com.smile.gatewayservice.filter;

import com.smile.gatewayservice.jwt.GatewayJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final GatewayJwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 헤더에서 'Authorization' 값을 읽어온다.
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        // 만약 토큰이 없거나, "Bearer "로 시작하지 않으면 다음 체인으로 요청을 전달한다.
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        // "Bearer " 접두어를 제거하고 순수 JWT 토큰만 추출한다.
        String token = authHeader.substring(7);

        // JWT 토큰의 유효성을 확인
        if(!jwtTokenProvider.validateToken(token)) {
            // 유효하지 않다면 401상태코드를 응답
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 토큰에서 ID와 Role정보를 추출한다.
        String id = jwtTokenProvider.getIdFromJWT(token);
        String role = jwtTokenProvider.getRoleFromJWT(token);

        // 기존 요청 객체를 복제(mutate)하고 헤더에 정보를 추가한다.
        ServerHttpRequest mutateRequest = exchange.getRequest().mutate()
                .header("X-User-Id", id)
                .header("X-User-Role", role)
                .build();

        // 변경된 요청 객체를 포함하는 새로운 ServerWebExchange를 생성
        ServerWebExchange mutatedExchange = exchange.mutate().request(mutateRequest).build();

        // 다음 필터로 요청 전달
        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
