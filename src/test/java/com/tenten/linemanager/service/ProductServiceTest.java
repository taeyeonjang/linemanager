package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.repository.ProductRepository;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

//    @BeforeEach
//    public void before() {
//        productService.init();
//    }

    @Test
    public void 제품생성() {
        //given
        Product productA = productService.createProduct();

        Product productB = productService.createProduct();

        //when
        String aNumber = productA.getSerialNumber();

        String bNumber = productB.getSerialNumber();

        //then
        Assertions.assertThat(aNumber).isNotEqualTo(bNumber);
    }

    @Test
    public void 시리얼넘버_조회() {
        //given
        Product productA = productService.createProduct();

        //when
        String aNumber = productA.getSerialNumber();

        //then
        Assertions.assertThat(productA).isEqualTo(productService.findOne(aNumber).orElseThrow());
    }

    @Test
    public void 상태_업데이트() {
        //given
        Product productA = productService.createProduct();

        //when

        productService.updateFinalResult(productA, ResultState.NG);

        //then

        Assertions.assertThat(productA.getFinalResult()).isEqualTo(ResultState.NG);
    }


    @Test
    public void 전체조회() {
        //given
        Product productA = productService.createProduct();
        Product productB = productService.createProduct();
        Product productC = productService.createProduct();
        Product productD = productService.createProduct();
        Product productE = productService.createProduct();

        //when
        //then

        Assertions.assertThat(productService.findAll().size()).isEqualTo(5);
    }

    @Test
    public void 중복_시리얼넘버() {
        Product productA = productService.createProduct();

        Assertions.assertThatThrownBy(() -> productService.createProduct(productA.getSerialNumber()))
                .isInstanceOf(IllegalStateException.class);
    }
}