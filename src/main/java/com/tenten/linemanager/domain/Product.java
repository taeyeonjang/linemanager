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

    @Column(unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private ResultState finalResult;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    private LineStatus status;

    private int currentProcessNo;

    public static Product create(String serialNumber) {
        Product product = new Product();
        product.serialNumber = serialNumber;
        product.finalResult = ResultState.INIT;
        product.createdAt = LocalDateTime.now();
        product.status = LineStatus.WAITING;
        product.currentProcessNo = 0;
        return product;
    }

    public void updateLineStatus() {
        this.status = LineStatus.RUNNING;
    }

    public void updateCurrentProcessNo(int processNo) {
        this.currentProcessNo = processNo;
    }

    public void updateFinal(ResultState result) {
        this.finalResult = result;
        this.completedAt = LocalDateTime.now();
        this.status = LineStatus.DONE;
    }
}
