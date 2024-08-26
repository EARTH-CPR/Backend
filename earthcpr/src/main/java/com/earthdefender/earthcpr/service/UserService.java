package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.UserDTO;
import com.earthdefender.earthcpr.model.User;
import com.earthdefender.earthcpr.repository.UserRepository;
import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.response.CustomException;
import com.earthdefender.earthcpr.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ApiService apiService;

    @Value("${shinhan.api.key}")
    private String shinhanApiKey;

    public void createUser(UserDTO.UserData userData) {
        Mono<UserDTO.UserResponse> response = apiService.postRequest("/member", userData.toShinhanRequest(shinhanApiKey), UserDTO.UserResponse.class);

        if (userRepository.existsByLoginId(userData.getLoginId())) {
            throw new CustomException(ErrorCode.USER_ID_DUPLICATE);
        }
        try {
            UserDTO.UserResponse tmp = response.block();
            userRepository.save(User.builder()
                    .loginId(userData.getLoginId())
                    .password(userData.getPassword())
                    .user_nickname(userData.getUserNickname())
                    .user_key(tmp.getUser_key())
                    .build());
            System.out.println(tmp);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}
