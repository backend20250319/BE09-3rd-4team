package com.smile.userservice.query.mapper;

import com.smile.userservice.query.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserDTO findUserById(Long id);
}
