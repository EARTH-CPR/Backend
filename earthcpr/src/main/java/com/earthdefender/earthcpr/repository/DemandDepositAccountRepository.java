package com.earthdefender.earthcpr.repository;

import com.earthdefender.earthcpr.model.DemandDepositAccount;
import com.earthdefender.earthcpr.model.DemandDepositProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DemandDepositAccountRepository extends JpaRepository<DemandDepositAccount, Long> {

}
