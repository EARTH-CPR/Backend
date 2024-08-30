package com.earthdefender.earthcpr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "savings_account")
public class SavingsAccount {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="saving_product_id")
    private SavingsProduct savingProduct;

    private String accountNo;

    private double additional_interest_rate;
    private String withdrawalAccountNo;

}
