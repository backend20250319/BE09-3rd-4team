package com.smile.userservice.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String userId;
    private String userName;
    private Integer age;
    private String gender;
    private String role;
}
