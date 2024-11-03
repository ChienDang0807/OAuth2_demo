package com.example.demo.service;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.request.RefreshRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AutheticationService  {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect  (IntrospectRequest request) throws JOSEException , ParseException;
    void  logout(LogoutRequest request) throws ParseException, JOSEException;
     AuthenticationResponse refreshToken (RefreshRequest request) throws ParseException, JOSEException;
}
