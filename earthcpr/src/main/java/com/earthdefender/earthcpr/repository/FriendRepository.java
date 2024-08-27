package com.earthdefender.earthcpr.repository;

import com.earthdefender.earthcpr.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
    //user1과 user2중 일치하는 userId가 있다면 나머지 아이디를 반환

    @Query("SELECT CASE WHEN f.user1.id = :userId THEN f.user2.id ELSE f.user1.id END " +
            "FROM Friend f WHERE f.user1.id = :userId OR f.user2.id = :userId")
    List<Long> findOtherUserIdsByUserId(Long userId);
}
