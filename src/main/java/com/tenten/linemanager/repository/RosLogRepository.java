package com.tenten.linemanager.repository;

import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RosLogRepository {

    private final EntityManager em;

    //저장
    public void save(RosLog rosLog) {
        em.persist(rosLog);
    }


    //시리얼넘버로 조회
    public Optional<RosLog> findByProductSerialNumber(String serialNumber) {
        return em.createQuery("select rl from RosLog rl where rl.product.serialNumber = :no", RosLog.class)
                .setParameter("no", serialNumber)
                .getResultStream()
                .findFirst();
    }


    //수동 결과 상태로 조회
    public List<RosLog> findOperatorDecision(ResultState state) {
        return em.createQuery("select rl from RosLog rl where rl.operatorDecision = :re", RosLog.class)
                .setParameter("re", state)
                .getResultList();
    }

}
