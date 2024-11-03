package com.example.demo.service.impl;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.entity.Permission;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.respository.PermissionRespository;
import com.example.demo.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRespository permissionRespository;
    PermissionMapper permissionMapper;

     public PermissionResponse create(PermissionRequest object){
            Permission permission = permissionMapper.toPermission(object);

            permission= permissionRespository.save(permission);

            return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permission = permissionRespository.findAll();
        return permission.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void  delete (String permission){
        permissionRespository.deleteById(permission);
    }
}
