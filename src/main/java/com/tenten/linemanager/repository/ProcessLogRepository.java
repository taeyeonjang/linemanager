package com.tenten.linemanager.repository;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.ResultState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

    public List<ProcessLog> findAll() {
        return em.createQuery("select pl from ProcessLog pl", ProcessLog.class)
                .getResultList();
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

    public List<ProcessLog> findByCriteria(String serialNumber, Integer processNo, ResultState result) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProcessLog> cq = cb.createQuery(ProcessLog.class);
        Root<ProcessLog> pl = cq.from(ProcessLog.class);

        List<Predicate> criteria = new ArrayList<>();

        if (StringUtils.hasText(serialNumber)) {
           Predicate serial = cb.equal(pl.get("product").get("serialNumber"), serialNumber);
           criteria.add(serial);
        }

        if (processNo != null) {
           Predicate pNo = cb.equal(pl.get("processNo"), processNo);
            criteria.add(pNo);
        }

        if (result != null) {
           Predicate pResult = cb.equal(pl.get("result"), result);
           criteria.add(pResult);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));

        return em.createQuery(cq).getResultList();
    }


}
