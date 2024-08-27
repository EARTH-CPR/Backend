package com.earthdefender.earthcpr.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Badge {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    @Column(name="badge_condition")
    private int condition;
}