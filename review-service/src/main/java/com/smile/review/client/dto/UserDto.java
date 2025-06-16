package com.smile.review.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String userId;
    private String userName;
    private Integer age;
    private String gender;
    private String role;
}