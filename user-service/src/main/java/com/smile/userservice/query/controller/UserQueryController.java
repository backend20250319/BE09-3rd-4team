package com.smile.userservice.query.controller;

import com.smile.userservice.auth.model.CustomUser;
import com.smile.userservice.common.ApiResponse;
import com.smile.userservice.query.dto.UserDetailsResponse;
import com.smile.userservice.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserDetail(
            @AuthenticationPrincipal String id
    ){
        UserDetailsResponse response = userQueryService.getUserDetail(Long.valueOf(id));
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
