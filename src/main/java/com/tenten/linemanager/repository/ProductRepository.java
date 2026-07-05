package com.tenten.linemanager.repository;

import com.tenten.linemanager.domain.LineStatus;
import com.tenten.linemanager.domain.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final EntityManager em;

    //jpa표준 방법은 @PersistanceContext / private EntityManger em  / 생성자 주입

    //저장 (생성)
    public void save(Product product) {
        em.persist(product);
    }

    //Id로 조회

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(em.find(Product.class, id));
    }

    //시리얼번호로 조회
    public Optional<Product> findBySerialNumber(String serialNum) {
        return em.createQuery("select p from Product p where p.serialNumber = :SN", Product.class)
                .setParameter("SN", serialNum )
                .getResultStream()
                .findFirst();
    }

    //전체 조회
    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    //상태 조회
    public List<Product> findByStatus(LineStatus status) {
        return em.createQuery("select p from Product p where p.status = :st", Product.class)
                .setParameter("st", status)
                .getResultList();
    }

//    public void init() {
//        em.createQuery("delete from Product").executeUpdate();
//    }

}
