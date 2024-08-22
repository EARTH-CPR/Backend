package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.UserDTO;
import com.earthdefender.earthcpr.model.User;
import com.earthdefender.earthcpr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(UserDTO.UserResponse userResponse) {
        userRepository.save(User.builder()
                .loginId(userResponse.getLogin_id())
                .user_key(userResponse.getUser_key())
                .password(userResponse.getPassword())
                .user_nickname(userResponse.getUser_nickname())
                .build());
    }

    public boolean checkDuplicatiedLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }




}
