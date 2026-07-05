package com.tenten.linemanager.repository;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.ResultState;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProcessLogRepository {

    private final EntityManager em;

    //저장
    public void save(ProcessLog processLog) {
        em.persist(processLog);
    }

    public Optional<ProcessLog> findById(Long id) {
        return Optional.ofNullable(em.find(ProcessLog.class, id));
    }

    //시리얼넘버 조회
    public List<ProcessLog> findByProductSerialNumber(String serialNumber) {
        return em.createQuery("select pl from ProcessLog pl where pl.product.serialNumber = :sn", ProcessLog.class)
                .setParameter("sn", serialNumber)
                .getResultList();
    }

    //특정 공정과 결과로 조회
    public List<ProcessLog> findByProcessNoAndState(int processNo, ResultState state) {
        return em.createQuery("select pl from ProcessLog pl where pl.processNo = :no and pl.result = :st", ProcessLog.class)
                .setParameter("no", processNo)
                .setParameter("st", state)
                .getResultList();
    }
}
