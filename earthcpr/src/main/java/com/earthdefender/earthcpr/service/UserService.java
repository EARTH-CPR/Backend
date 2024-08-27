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
    public ResponseEntity<String> loginUser(UserDTO.UserLoginRequest loginRequest, HttpSession session) {
        User user = userRepository.findByLoginId(loginRequest.getLogin_id());
        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid login credentials");
        }
        session.setAttribute("userId", user.getId());
        return ResponseEntity.ok("Login successful");
    }

    public ResponseEntity<String> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}
