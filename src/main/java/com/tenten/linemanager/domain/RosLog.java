package com.tenten.linemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class RosLog {
    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private ResultState operatorDecision;

    @Enumerated(EnumType.STRING)
    private ResultState rosState;

    private LocalDateTime decidedAt;
}
