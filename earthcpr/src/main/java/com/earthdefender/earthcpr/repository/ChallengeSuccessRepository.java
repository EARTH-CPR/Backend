package com.earthdefender.earthcpr.repository;

import com.earthdefender.earthcpr.model.ChallengeSuccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeSuccessRepository extends JpaRepository<ChallengeSuccess, Long> {

}