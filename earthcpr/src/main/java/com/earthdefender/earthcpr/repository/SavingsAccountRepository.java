package com.earthdefender.earthcpr.repository;

import com.earthdefender.earthcpr.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    Optional<SavingsAccount> findByAccountNo(String accountNo);
}
