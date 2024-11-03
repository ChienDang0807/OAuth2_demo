package com.example.demo.controller;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.service.PermissionService;
import com.example.demo.service.impl.PermissionServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ResponseData<PermissionResponse> create (@RequestBody PermissionRequest request){
        return  new ResponseData<>(HttpStatus.OK.value(),
                "Permission added successfully",
                permissionService.create(request));
    }

    @GetMapping
    ResponseData<List<PermissionResponse>> getAll(){
        return  new ResponseData<>(HttpStatus.OK.value()
                ,"Get all users successfully"
                ,permissionService.getAll());
    }
    @DeleteMapping("/{permission}")
    ResponseData<?> delete(@PathVariable String permission){
        permissionService.delete(permission);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Delete permission successfully")
                .build();
    }
}
