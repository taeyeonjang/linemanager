package com.tenten.linemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
public class Product {

    @Id @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @Column(unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private ResultState finalResult;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;


    public static Product create(String serialNumber) {
        Product product = new Product();
        product.serialNumber = serialNumber;
        product.finalResult = ResultState.INIT;
        product.createdAt = LocalDateTime.now();

        return product;
    }

    public void update(ResultState result) {
        this.finalResult = result;
        this.completedAt = LocalDateTime.now();
    }
}
