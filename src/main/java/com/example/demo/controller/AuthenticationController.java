package com.example.demo.controller;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.request.RefreshRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.service.AutheticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {

    AutheticationService autheticationService;

    @PostMapping("/token")
    ResponseData<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request){
        return new ResponseData<>(HttpStatus.OK.value(),"OK", autheticationService.authenticate(request)) ;
    }

    @PostMapping("/refresh")
    ResponseData<AuthenticationResponse> refreshToken (@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return new ResponseData<>(HttpStatus.OK.value(),"OK", autheticationService.refreshToken(request)) ;
    }

    @PostMapping("/introspect")
    ResponseData<IntrospectResponse> introspect (@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return new ResponseData<>(HttpStatus.OK.value(),"OK", autheticationService.introspect(request)) ;
    }

    @PostMapping("/logout")
    ResponseData<?> logout (@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        autheticationService.logout(request);
        return new ResponseData<>(HttpStatus.OK.value(),"Logout thành công") ;
    }
}
