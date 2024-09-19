package com.earthdefender.earthcpr.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class ChallengeSuccess {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="savings_account_id")
    private SavingsAccount savingsAccount;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private LocalDateTime challangeSuccessDate;

}