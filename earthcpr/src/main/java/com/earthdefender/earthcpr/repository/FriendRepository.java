package com.earthdefender.earthcpr.repository;

import com.earthdefender.earthcpr.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Object findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}
