package com.smile.userservice.query.service;

import com.smile.userservice.query.dto.UserDTO;
import com.smile.userservice.query.dto.UserDetailsResponse;
import com.smile.userservice.query.dto.UserModifyRequest;
import com.smile.userservice.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsResponse getUserDetail(String userId) {
        UserDTO user = Optional.ofNullable(
                userMapper.findUserById(userId)
        ).orElseThrow(() -> new RuntimeException("사용자 정보를 찾지 못했습니다."));

        System.out.println("userId = " + user.getUserId());
        return UserDetailsResponse.builder().user(user).build();
    }

    public UserDetailsResponse modifyUser(String userId, UserModifyRequest request) {

        String rawPassword = request.getUserPwd();
        String encodedPassword = null;
        Long id = request.getId();

        if(rawPassword != null && !rawPassword.isBlank()) {
            encodedPassword = passwordEncoder.encode(rawPassword);
        }

        int updated = userMapper.updateUserById(
                id,
                userId,
                encodedPassword,
                request.getUserName(),
                request.getAge(),
                request.getGender(),
                request.getRole()
        );

        if (updated == 0) {
            throw new RuntimeException("사용자 정보를 찾지 못했거나 수정되지 않았습니다.");
        }

        UserDTO updatedUser = userMapper.findUserById(userId);
        return UserDetailsResponse.builder().user(updatedUser).build();
    }

    public UserDetailsResponse deleteUser(String userId) {

        UserDTO user = Optional.ofNullable(userMapper.findUserById(userId))
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾지 못했습니다."));

        int deleted = userMapper.deleteUserById(userId);
        if (deleted == 0) {
            throw new RuntimeException("사용자 삭제에 실패했습니다.");
        }

        return UserDetailsResponse.builder().user(user).build();
    }

}
