package com.earthdefender.earthcpr.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ChallengeSuccess {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="savings_product_id")
    private SavingsProduct saving;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private LocalDateTime challange_success_date;

}