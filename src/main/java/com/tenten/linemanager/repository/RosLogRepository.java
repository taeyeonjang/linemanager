package com.tenten.linemanager.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linemanager.domain.QRosLog;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import com.tenten.linemanager.dto.PageDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RosLogRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final QRosLog rl = QRosLog.rosLog;
    private static final int PAGE_SIZE = 20;

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

    public PageDto<RosLog> findByQueryDsl(String serialNumber, ResultState result, int page) {

        PageDto<RosLog> dto = new PageDto<>();
        dto.setList(
                queryFactory.selectFrom(rl)
               .where(
                       serialNumberEq(serialNumber),
                       resultEq(result)
               )
               .offset((page - 1) * PAGE_SIZE).limit(PAGE_SIZE)
               .fetch()
        );
        dto.setCount(
                queryFactory.selectFrom(rl)
                .where(
                        serialNumberEq(serialNumber),
                        resultEq(result)
                )
                .fetchCount()
        );

        dto.setTotalPages((dto.getCount() + PAGE_SIZE - 1) / PAGE_SIZE);

        return dto;
    }

    private BooleanExpression serialNumberEq(String serialNumber) {
        return StringUtils.hasText(serialNumber) ? rl.product.serialNumber.eq(serialNumber.toUpperCase()) : null;
    }

    private BooleanExpression resultEq(ResultState result ) {
        return result != null ? rl.operatorDecision.eq(result) : null;
    }

}
