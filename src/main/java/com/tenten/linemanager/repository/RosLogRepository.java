package com.tenten.linemanager.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linemanager.domain.QRosLog;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.models.xml.internal.QueryProcessing;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RosLogRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final QRosLog rl = QRosLog.rosLog;

    //저장
    public void save(RosLog rosLog) {
        em.persist(rosLog);
    }

    public Optional<RosLog> findById(Long id) {
        return Optional.ofNullable(em.find(RosLog.class, id));
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

    public List<RosLog> findAll() {
        return em.createQuery("select rl from RosLog rl", RosLog.class)
                .getResultList();
    }

    public List<RosLog> findByCriteria(String serialNumber, ResultState result) {

       return queryFactory.selectFrom(rl)
               .where(
                       serialNumberEq(serialNumber.toUpperCase()),
                       resultEq(result)
               )
               .fetch();
    }

    private BooleanExpression serialNumberEq(String serialNumber) {
        return StringUtils.hasText(serialNumber) ? rl.product.serialNumber.eq(serialNumber) : null;
    }

    private BooleanExpression resultEq(ResultState result ) {
        return result != null ? rl.operatorDecision.eq(result) : null;
    }

}
