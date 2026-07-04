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

    private LocalDateTime completedAt;


    public static ProcessLog create(Product product, int processNo) {
        ProcessLog processLog = new ProcessLog();
        processLog.product = product;
        processLog.processNo = processNo;
        processLog.result = ResultState.INIT;
        processLog.processedAt = LocalDateTime.now();

        return processLog;
    }

    public void update(ResultState result) {
        this.result = result;
        completedAt = LocalDateTime.now();
    }
}
