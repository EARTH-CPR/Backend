package com.earthdefender.earthcpr.repository;

import com.earthdefender.earthcpr.model.ChallengeSuccess;
import com.earthdefender.earthcpr.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeSuccessRepository extends JpaRepository<ChallengeSuccess, Long> {
    List<ChallengeSuccess> findBySavingsAccount(SavingsAccount savingsAccount);
}