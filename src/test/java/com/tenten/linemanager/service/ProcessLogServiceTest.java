package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.repository.ProcessLogRepository;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProcessLogServiceTest {

    @Autowired
    ProcessLogService processLogService;

    @Autowired
    ProductService productService;

    @Test
    public void 생성() {
        //given
        Product product = productService.createProduct();
        ProcessLog processLogA = processLogService.createProcessLog(product.getId(), 1);

        //when
        String serialNumber = processLogA.getProduct().getSerialNumber();

        //then
        Assertions.assertThat(processLogService.findOne(serialNumber)).contains(processLogA);
    }

    @Test
    public void 완료시간() {

        Product product = productService.createProduct();

        ProcessLog processLogA = processLogService.createProcessLog(product.getId(), 1);

        processLogService.updateProcessResult(processLogA.getId(), ResultState.OK);

        Assertions.assertThat(processLogA.getCompletedAt()).isNotNull();
        Assertions.assertThat(processLogA.getResult()).isEqualTo(ResultState.OK);
    }

    @Test
    public void 공정_결과_조회() {

        Product productA = productService.createProduct();

        Long productId = productA.getId();

        ProcessLog processLogA = processLogService.createProcessLog(productId, 1);
        processLogService.createProcessLog(productId, 2);
        processLogService.createProcessLog(productId, 3);
        processLogService.createProcessLog(productId, 4);

        Product productB = productService.createProduct();
        Product productC = productService.createProduct();

        ProcessLog processLogB = processLogService.createProcessLog(productB.getId(), 1);
        ProcessLog processLogC = processLogService.createProcessLog(productC.getId(), 1);

        processLogService.updateProcessResult(processLogA.getId(), ResultState.NG);
        processLogService.updateProcessResult(processLogB.getId(), ResultState.NG);
        processLogService.updateProcessResult(processLogC.getId(), ResultState.NG);

//        List<ProcessLog> list = processLogService.findProcess(1, ResultState.NG);

//        Assertions.assertThat(list.size()).isEqualTo(3);
    }
}