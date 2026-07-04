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
    private LocalDateTime completedAt;

    public static RosLog create(Product product) {
        RosLog rosLog = new RosLog();
        rosLog.product = product;
        rosLog.operatorDecision = ResultState.INIT;
        rosLog.rosState = ResultState.INIT;
        rosLog.decidedAt = LocalDateTime.now();

        return rosLog;
    }

    public void update(ResultState result) {
        this.operatorDecision = result;
        this.completedAt = LocalDateTime.now();
    }
}
