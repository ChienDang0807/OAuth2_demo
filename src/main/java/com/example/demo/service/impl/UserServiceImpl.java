package com.example.demo.service.impl;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.exceptions.AppExceptions;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.respository.RoleRespository;
import com.example.demo.respository.SearchRepository;
import com.example.demo.respository.UserRespository;
import com.example.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

     UserRespository userRespository;
     RoleRespository roleRespository;
     UserMapper userMapper;
     PasswordEncoder passwordEncoder;
     SearchRepository searchRepository;

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        if(userRespository.existsByUsername(request.getUsername())){
            throw  new AppExceptions(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        //user.setRoles(new HashSet<>()(roles));

        return userMapper.toUserResponse(userRespository.save(user));
    }
    @Override
    public  UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name= context.getAuthentication().getName();

        User user = userRespository.findByUsername(name)
                .orElseThrow(() -> new AppExceptions(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }



    @Override
    @PostAuthorize("returnObject.username == authentication.name") // chạy method trước mới kiểm tra quyền
    public Optional<UserResponse> getUser(Integer id) {
       return  userRespository.findById(id).map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse updateUser(Integer id, UserUpdateRequest request) {
        Optional<User> userOptional = userRespository.findById(id);
        // Trong trường hợp Optional có 2 trường hợp nên phải có if else
        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();  // Access the User object

            userMapper.updateUser(userToUpdate,request);
            userToUpdate.setPassword(passwordEncoder.encode(request.getPassword())); // ma hoa mk

            var roles = roleRespository.findAllById(request.getRoles());
            userToUpdate.setRoles(new HashSet<>(roles));

            return userMapper.toUserResponse(userRespository.save(userToUpdate));
        } else {
            throw  new AppExceptions(ErrorCode.USER_NOT_FOUND);
        }

    }

    @Override
    public void deleteUser(Integer id) {
       userRespository.deleteById(id);
    }

    @Override
    // @PreAuthorize : tao ra 1 Aop bọc bên ngoài method, kieem tra quyền trc mới chạy method
    @PreAuthorize("hasRole('ADMIN')") // hasRole match vs cái nào có prefix là role
    //PreAuthorize("hasAuthority('UPDATE_DATA'))  : match vs cái nào == UPDATE_DATA
    public PageResponse<?> getAllUsers(int pageNo, int pageSize) {
        int p =0;
        if(pageNo>0){
            p = pageNo -1;
        }

       Pageable pageable = PageRequest.of(p,pageSize);

       Page<User> users = userRespository.findAll(pageable);


        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage( users.getTotalPages())
                .items(users)
                .build();
       // return users.stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public PageResponse<?> getAllUsersSort(int pageNo, int pageSize, String sortBy) {
        int p =0;
        if(pageNo>0){
            p = pageNo -1;
        }

        List<Sort.Order> sorts = new ArrayList<>();

        if(StringUtils.hasLength(sortBy)){

            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if (matcher.group(3).equalsIgnoreCase("asc")){
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(p,pageSize, Sort.by(sorts)); // sortBy là trường để sắp xếp

        Page<User> users = userRespository.findAll(pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .items(users)
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int p =0;
        if(pageNo>0){
            p = pageNo -1;
        }

        List<Sort.Order> orders = new ArrayList<>();

        if (sorts != null){
            for (String sortBy : sorts){
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if(matcher.find()){
                    if (matcher.group(3).equalsIgnoreCase("asc")){
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    }else {
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }


        Pageable pageable = PageRequest.of(p,pageSize, Sort.by(orders)); // sortBy là trường để sắp xếp

        Page<User> users = userRespository.findAll(pageable);

        return PageResponse.builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .totalPage(users.getTotalPages())
                .items(users)
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersSortByColumnsAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        return searchRepository.getAllUsersSortByColumnsAndSearch(pageNo,pageSize,search,sortBy);
    }

    @Override
    public PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        return searchRepository.advanceSearchUser(pageNo,pageSize,sortBy,search);
    }


}

