package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;


import java.util.Optional;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    UserResponse getMyInfo();


    Optional<UserResponse> getUser(Integer id);

    UserResponse updateUser(Integer id, UserUpdateRequest userUpdateRequest);

    void deleteUser (Integer id);

    PageResponse<?> getAllUsers(int pageNo, int pageSize);

    PageResponse<?> getAllUsersSort(int pageNo, int pageSize,String sortBy);

    PageResponse<?> getAllUsersSortByMultipleColumns(int pageNo, int pageSize, String... sorts);

    PageResponse<?> getAllUsersSortByColumnsAndSearch(int pageNo, int pageSize,String search, String sortBy);

    PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize,String sortBy, String... search) ;
}
