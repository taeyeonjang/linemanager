package com.tenten.linemanager.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linemanager.config.QueryDslConfig;
import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.QProcessLog;
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
    private final JPAQueryFactory queryFactory;

    private final QProcessLog pl = QProcessLog.processLog;

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
        return queryFactory.selectFrom(pl)
                .where(
                        serialNumberEq(serialNumber.toUpperCase()),
                        processNoEq(processNo),
                        resultEq(result)
                )
                .fetch();
    }

    private BooleanExpression serialNumberEq(String serialNumber) {
        return StringUtils.hasText(serialNumber) ? pl.product.serialNumber.eq(serialNumber) : null;
    }

    private BooleanExpression processNoEq(Integer processNo) {
        return processNo != null ? pl.processNo.eq(processNo) : null;
    }

    private BooleanExpression resultEq(ResultState result) {
        return result != null ? pl.result.eq(result) : null;
    }
}
