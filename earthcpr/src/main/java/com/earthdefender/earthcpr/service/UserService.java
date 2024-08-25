package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.UserDTO;
import com.earthdefender.earthcpr.model.User;
import com.earthdefender.earthcpr.repository.UserRepository;
import com.earthdefender.earthcpr.response.CustomException;
import com.earthdefender.earthcpr.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ApiService apiService;

    @Value("${shinhan.api.key}")
    private String shinhanApiKey;

    public ResponseEntity<String> createUser(UserDTO.UserData userData) {
        userData.setApiKey(shinhanApiKey);
        Mono<UserDTO.UserResponse> response = apiService.postRequest("/member", userData.toShinhanRequest(), UserDTO.UserResponse.class);

        if (userRepository.existsByLoginId(userData.getLogin_id())) {
            return ResponseEntity.badRequest().body("Login ID already exists");
        }
        try {
            UserDTO.UserResponse tmp = response.block();
            userRepository.save(User.builder()
                    .loginId(userData.getLogin_id())
                    .password(userData.getPassword())
                    .user_nickname(userData.getUser_nickname())
                    .user_key(tmp.getUser_key())
                    .build());
            System.out.println(tmp);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return ResponseEntity.ok("User creation in progress");
    }
}
