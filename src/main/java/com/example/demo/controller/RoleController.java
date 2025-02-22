package com.example.demo.controller;

import com.example.demo.dto.request.RoleRequest;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.RoleResponse;
import com.example.demo.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ResponseData<RoleResponse> create (@RequestBody RoleRequest request){
        return  new ResponseData<>(HttpStatus.OK.value(),
                "Role added successfully",
                roleService.create(request));
    }

    @GetMapping
    ResponseData<List<RoleResponse>> getAll(){
        return  new ResponseData<>(HttpStatus.OK.value()
                ,"Get all roles successfully"
                ,roleService.getAll());
    }
    @DeleteMapping("/{role}")
    ResponseData<?> delete(@PathVariable String role){
        roleService.delete(role);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Delete role successfully")
                .build();
    }
}
