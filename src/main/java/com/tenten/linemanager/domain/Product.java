package com.tenten.linemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Product {

    @Id @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private ResultState finalResult;

    private LocalDateTime createdAt;

}
