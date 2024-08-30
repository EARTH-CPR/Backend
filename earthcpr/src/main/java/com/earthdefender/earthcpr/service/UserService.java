package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.UserDTO;
import com.earthdefender.earthcpr.model.User;
import com.earthdefender.earthcpr.repository.UserRepository;
import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.response.CustomException;
import com.earthdefender.earthcpr.response.ErrorCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
                    .userNickname(userData.getUserNickname())
                    .userKey(tmp.getUserKey())
                    .build());
            System.out.println(tmp);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
    public void loginUser(UserDTO.UserLoginRequest loginRequest) {
        Optional<User> user = userRepository.findByLoginId(loginRequest.getLoginId());
        if (user.isEmpty() || !user.get().getPassword().equals(loginRequest.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

}
