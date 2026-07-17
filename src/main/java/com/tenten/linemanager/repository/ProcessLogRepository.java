package com.tenten.linemanager.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linemanager.config.QueryDslConfig;
import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.QProcessLog;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.dto.PageDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProcessLogRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final QProcessLog pl = QProcessLog.processLog;
    private static final int PAGE_SIZE = 20;

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

    public PageDto<ProcessLog> findByQueryDsl(String serialNumber, Integer processNo, ResultState result, int page) {
        PageDto<ProcessLog> dto = new PageDto<>();
        dto.setList(
                queryFactory.selectFrom(pl)
                        .where(
                                serialNumberEq(serialNumber),
                                processNoEq(processNo),
                                resultEq(result)
                        )
                        .offset((page - 1) * PAGE_SIZE).limit(PAGE_SIZE)
                        .fetch()
        );
        dto.setCount(
                queryFactory.selectFrom(pl)
                        .where(
                                serialNumberEq(serialNumber),
                                processNoEq(processNo),
                                resultEq(result)
                        )
                        .fetchCount()
        );
        dto.setTotalPages((dto.getCount() + PAGE_SIZE - 1) / PAGE_SIZE);
        return dto;
    }


    private BooleanExpression serialNumberEq(String serialNumber) {
        return StringUtils.hasText(serialNumber) ? pl.product.serialNumber.eq(serialNumber.toUpperCase()) : null;
    }

    private BooleanExpression processNoEq(Integer processNo) {
        return processNo != null ? pl.processNo.eq(processNo) : null;
    }

    private BooleanExpression resultEq(ResultState result) {
        return result != null ? pl.result.eq(result) : null;
    }
}
