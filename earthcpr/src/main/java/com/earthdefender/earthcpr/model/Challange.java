package com.earthdefender.earthcpr.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Challange {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="badge_id")
    private Badge badge;

    private String name;
    private int type;
    private String info;
    private int verification;
}
