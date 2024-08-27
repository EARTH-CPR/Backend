package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.FriendDTO;
import com.earthdefender.earthcpr.model.Friend;
import com.earthdefender.earthcpr.model.User;
import com.earthdefender.earthcpr.repository.ChallengeSuccessRepository;
import com.earthdefender.earthcpr.repository.FriendRepository;
import com.earthdefender.earthcpr.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final ChallengeSuccessRepository challengeSuccessRepository;

    private final UserRepository userRepository;

    public ResponseEntity<String> addFriend(FriendDTO.FriendData friendData) {
        //user1과 user2가 그대로 데이터베이스에 있거나 user1과 user2가 반대로 데이터베이스에 있을 경우
        if (friendRepository.findByUser1IdAndUser2Id(friendData.getUser1Id(), friendData.getUser2Id())!=null|| friendRepository.findByUser1IdAndUser2Id(friendData.getUser2Id(), friendData.getUser1Id())!=null) {
            return ResponseEntity.badRequest().body("이미 친구입니다.");
        }
        try{
            Optional<User> friend1 = userRepository.findById(friendData.getUser1Id());
            Optional<User> friend2 = userRepository.findById(friendData.getUser2Id());
            if(!friend1.isPresent() || !friend2.isPresent()){
                return ResponseEntity.badRequest().body("존재하지 않는 사용자입니다.");
            }
            friendRepository.save(Friend.builder()
                    .user1(friend1.get())
                    .user2(friend2.get())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("친구 추가 실패");
        }

        return ResponseEntity.ok("친구 추가 성공");
    }

    public ResponseEntity<String> deleteFriend(FriendDTO.FriendData friendData) {
        Friend friend = friendRepository.findByUser1IdAndUser2Id(friendData.getUser1Id(), friendData.getUser2Id());
        if (friend == null) {
            friend = friendRepository.findByUser1IdAndUser2Id(friendData.getUser2Id(), friendData.getUser1Id());
        }
        if(friend==null){
            return ResponseEntity.badRequest().body("친구가 아닙니다.");
        }
        try{
            friendRepository.delete(friend);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("친구 삭제 실패");
        }

        return ResponseEntity.ok("친구 삭제 성공");
    }

}
