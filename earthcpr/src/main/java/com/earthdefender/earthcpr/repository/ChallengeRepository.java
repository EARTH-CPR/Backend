package com.earthdefender.earthcpr.repository;

import com.earthdefender.earthcpr.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
