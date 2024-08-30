package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.FriendDTO;
import com.earthdefender.earthcpr.model.Friend;
import com.earthdefender.earthcpr.model.User;
import com.earthdefender.earthcpr.repository.ChallengeSuccessRepository;
import com.earthdefender.earthcpr.repository.FriendRepository;
import com.earthdefender.earthcpr.repository.UserRepository;
import com.earthdefender.earthcpr.response.CustomException;
import com.earthdefender.earthcpr.response.ErrorCode;
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

    public void addFriend(FriendDTO.FriendData friendData) {
        //user1과 user2가 그대로 데이터베이스에 있거나 user1과 user2가 반대로 데이터베이스에 있을 경우
        if (friendRepository.findByUser1IdAndUser2Id(friendData.getUser1Id(), friendData.getUser2Id())!=null|| friendRepository.findByUser1IdAndUser2Id(friendData.getUser2Id(), friendData.getUser1Id())!=null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        try{
            Optional<User> friend1 = userRepository.findById(friendData.getUser1Id());
            Optional<User> friend2 = userRepository.findById(friendData.getUser2Id());
            if(!friend1.isPresent() || !friend2.isPresent()){
                throw new CustomException(ErrorCode.BAD_REQUEST);            }
            friendRepository.save(Friend.builder()
                    .user1(friend1.get())
                    .user2(friend2.get())
                    .build());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

    }

    public void deleteFriend(FriendDTO.FriendData friendData) {
        Friend friend = friendRepository.findByUser1IdAndUser2Id(friendData.getUser1Id(), friendData.getUser2Id());
        if (friend == null) {
            friend = friendRepository.findByUser1IdAndUser2Id(friendData.getUser2Id(), friendData.getUser1Id());
        }
        if(friend==null){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        try{
            friendRepository.delete(friend);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

}
