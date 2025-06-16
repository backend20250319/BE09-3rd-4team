package com.smile.userservice.query.controller;

import com.smile.userservice.common.ApiResponse;
import com.smile.userservice.query.dto.UserDetailsResponse;
import com.smile.userservice.query.dto.UserModifyRequest;
import com.smile.userservice.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    // 로그인된 사용자 정보 조회
    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserDetail(
            @AuthenticationPrincipal String id
    ){
        UserDetailsResponse response = userQueryService.getUserDetail(Long.valueOf(id));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // userId를 통한 사용자 정보 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUser(@PathVariable("userId") String userId) {
        UserDetailsResponse response = userQueryService.getUserInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 사용자 정보 수정
    @PutMapping("/users/modify")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> modifyUser(
        @AuthenticationPrincipal String id,
        @RequestBody UserModifyRequest request
    ){
        UserDetailsResponse response = userQueryService.modifyUser(Long.valueOf(id), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 사용자 정보 삭제
    @DeleteMapping("/users/delete")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> deleteUser(
            @AuthenticationPrincipal String id
    ){
        UserDetailsResponse response = userQueryService.deleteUser(Long.valueOf(id));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
