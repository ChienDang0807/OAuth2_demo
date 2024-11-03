package com.example.demo.mapper;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    // @MappingTarget cập nhật đối tượng thay vì tạo đối tượng mới
    @Mapping(target = "roles" ,ignore = true) // k gen thuoc tinh roles
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
