package com.smile.recommendservice.dto;

import lombok.Data;

//사용자 정보 DTO

@Data
public class UserDto {
    private Long id;
    private String gender;
    private String ageGroup;
}