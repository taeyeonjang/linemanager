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
        ProcessLog processLogA = processLogService.createProcessLog(productService.createProduct(), 1, ResultState.OK);

        //when
        String serialNumber = processLogA.getProduct().getSerialNumber();

        //then
        Assertions.assertThat(processLogService.findOne(serialNumber)).contains(processLogA);
    }

    @Test
    public void 완료시간() {

        ProcessLog processLogA = processLogService.createProcessLog(productService.createProduct(), 1, ResultState.OK);

        processLogService.updateCompltedAt(processLogA);

        Assertions.assertThat(processLogA.getCompletedAt()).isNotNull();
    }

    @Test
    public void 공정_결과_조회() {
        ProcessLog processLogA = processLogService.createProcessLog(productService.createProduct(), 1, ResultState.OK);
        processLogService.createProcessLog(processLogA.getProduct(), 2, ResultState.OK);
        processLogService.createProcessLog(processLogA.getProduct(), 3, ResultState.OK);
        processLogService.createProcessLog(processLogA.getProduct(), 4, ResultState.NG);

        processLogService.createProcessLog(productService.createProduct(), 1, ResultState.OK);
        processLogService.createProcessLog(productService.createProduct(), 1, ResultState.NG);
        processLogService.createProcessLog(productService.createProduct(), 1, ResultState.NG);
        processLogService.createProcessLog(productService.createProduct(), 1, ResultState.NG);

        List<ProcessLog> list = processLogService.findProcess(1, ResultState.NG);

        Assertions.assertThat(list.size()).isEqualTo(3);
    }
}