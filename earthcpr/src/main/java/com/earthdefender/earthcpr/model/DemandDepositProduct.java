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
@Table(name = "deposit_product")
public class DemandDepositProduct {
    @Id
    @GeneratedValue
    private Long id;
    private String bankCode;
    private String accountName;
    private String accountDescription;
    private String accountTypeUniqueNo;
}
