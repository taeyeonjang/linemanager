package com.tenten.linemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class ProcessLog {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int processNo;

    @Enumerated(EnumType.STRING)
    private ResultState result;

    private LocalDateTime processedAt;

}
