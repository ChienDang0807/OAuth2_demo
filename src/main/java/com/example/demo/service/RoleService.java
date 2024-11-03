package com.example.demo.service;

import com.example.demo.dto.request.RoleRequest;
import com.example.demo.dto.response.RoleResponse;
import com.example.demo.entity.Role;

import java.util.List;

public interface RoleService {
    RoleResponse create (RoleRequest request);
    List<RoleResponse> getAll();
    void delete (String role);

}
