package com.example.demo.controller;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping
    public ResponseData<UserResponse> createController(@RequestBody @Valid UserCreationRequest request){
        return new ResponseData<>(HttpStatus.CREATED.value(), "Used added successfully! ", userService.createUser(request));
    }

    @GetMapping("/get-all-user")
    public ResponseData<PageResponse<?>> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                  @Min(5) @RequestParam(defaultValue = "20", required = false) int pageSize)
    {
        return new ResponseData<>(HttpStatus.OK.value(), "List users are found", userService.getAllUsers(pageNo,pageSize));
    }
    @GetMapping("/get-all-user-sort")
    public ResponseData<PageResponse<?>> getAllUsersSort(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                        @Min(5) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                        @RequestParam( required = false) String sortBy)
    {
        return new ResponseData<>(HttpStatus.OK.value(), "List users are found", userService.getAllUsersSort(pageNo,pageSize,sortBy));
    }

    @GetMapping("/get-all-user-sort-multiple-column")
    public ResponseData<PageResponse<?>> getAllUsersSortMultipleColumns(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                            @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                            @RequestParam( required = false) String... sortBy)
    {
        return new ResponseData<>(HttpStatus.OK.value(), "List users are found", userService.getAllUsersSortByMultipleColumns(pageNo,pageSize,sortBy));
    }

    @GetMapping("/get-all-user-sort-by-column-search")
    public ResponseData<PageResponse<?>> getAllUsersSortByColumnsAndSearch(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                           @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                           @RequestParam( required = false) String search,
                                                                           @RequestParam( required = false) String sortBy)
    {
        return new ResponseData<>(HttpStatus.OK.value(), "List users are found", userService.getAllUsersSortByColumnsAndSearch(pageNo,pageSize,search,sortBy));
    }

    @GetMapping("/advance-search-by-criteria")
    public ResponseData<PageResponse<?>> advanceSearchByCriteria(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                 @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                 @RequestParam( required = false) String sortBy,
                                                                 @RequestParam( required = false) String... search)
    {
        return new ResponseData<>(HttpStatus.OK.value(), "List users are found", userService.advanceSearchByCriteria(pageNo,pageSize,sortBy,search));
    }


    @GetMapping(path = "/{userId}")
    public ResponseData<?> getUserById(@PathVariable("userId") Integer userId){
        // với kiểu giữ liệu trả về Optional có thêm orElse
        return  new ResponseData<>(HttpStatus.OK.value(), "User is found", userService.getUser(userId));
    }

    @GetMapping(path = "/myInfo")
    public ResponseData<UserResponse> getMyInfo(){

        return  new ResponseData<>(HttpStatus.OK.value(), "User is found", userService.getMyInfo());
    }

    @PutMapping(path = "/{userId}")
    public ResponseData<UserResponse> updateUser (@PathVariable("userId") Integer userId,@RequestBody UserUpdateRequest request){
        return new ResponseData<>(HttpStatus.ACCEPTED.value(),"User updated successfully! ",userService.updateUser(userId,request));
    }

    @DeleteMapping(path =  "/{userId}")
    public  ResponseData<?> deleteUser (@ PathVariable("userId") Integer userId){
        userService.deleteUser(userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User is deleted");
    }
}
