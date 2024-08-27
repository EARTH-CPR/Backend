package com.earthdefender.earthcpr.controller;

import com.earthdefender.earthcpr.DTO.FriendDTO;
import com.earthdefender.earthcpr.service.FriendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@Valid @RequestBody FriendDTO.FriendData friendData) {
        return friendService.addFriend(friendData);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFriend(@Valid @RequestBody FriendDTO.FriendData friendData) {
        return friendService.deleteFriend(friendData);
    }

}
