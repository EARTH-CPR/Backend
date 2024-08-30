package com.earthdefender.earthcpr.controller;

import com.earthdefender.earthcpr.DTO.UserDTO;
import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import java.util.SortedMap;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseEntity> createUser(@Valid @RequestBody UserDTO.UserData userdata) {
        userService.createUser(userdata);
        return ApiResponseEntity.toResponseEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseEntity> loginUser(@Valid @RequestBody UserDTO.UserLoginRequest loginRequest) {
        userService.loginUser(loginRequest);
        return ApiResponseEntity.toResponseEntity();
    }

}
