package com.earthdefender.earthcpr.controller;

import com.earthdefender.earthcpr.DTO.UserDTO;
import com.earthdefender.earthcpr.repository.UserRepository;
import com.earthdefender.earthcpr.response.CustomException;
import com.earthdefender.earthcpr.response.ErrorCode;
import com.earthdefender.earthcpr.service.ApiService;
import com.earthdefender.earthcpr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.SortedMap;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ApiService apiService;

    @Value("${shinhan.api.key}")
    private String shinhanApiKey;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserDTO.UserRequest request) {
        if (userService.checkDuplicatiedLoginId(request.getLogin_id())) {
            //throw new Custo
            return ResponseEntity.badRequest().body("Login ID already exists");
        }
        UserDTO.UserShinhanRequest shinhanRequest = UserDTO.UserShinhanRequest.builder()
                .login_id(request.getLogin_id())
                .apiKey(shinhanApiKey)
                .build();

        String password = request.getPassword();
        String user_nickname = request.getUser_nickname();

        Mono<UserDTO.UserResponse> response = apiService.postRequest("/member", shinhanRequest, UserDTO.UserResponse.class);
        try {
            UserDTO.UserResponse tmp = response.block();
            System.out.println(tmp);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

//        response
//                .subscribe(
//                        userResponse -> {
//                            userResponse.setPassword(password);
//                            userResponse.setUser_nickname(user_nickname);
//                            System.out.println("receive: " + userResponse);
//                            userService.save(userResponse);
//                        }
//                );
        return ResponseEntity.ok("User creation in progress");
    }


}
