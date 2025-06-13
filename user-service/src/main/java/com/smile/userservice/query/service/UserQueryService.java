package com.smile.userservice.query.service;

import com.smile.userservice.query.dto.UserDTO;
import com.smile.userservice.query.dto.UserDetailsResponse;
import com.smile.userservice.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserMapper userMapper;

    public UserDetailsResponse getUserDetail(Long userId) {
        UserDTO user = Optional.ofNullable(
                userMapper.findUserById(userId)
        ).orElseThrow(() -> new RuntimeException("사용자 정보를 찾지 못했습니다."));

        return UserDetailsResponse.builder().user(user).build();
    }
}
