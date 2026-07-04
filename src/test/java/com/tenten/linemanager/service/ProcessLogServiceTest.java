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
        ProcessLog processLogA = processLogService.createProcessLog(productService.createProduct(), 1);

        //when
        String serialNumber = processLogA.getProduct().getSerialNumber();

        //then
        Assertions.assertThat(processLogService.findOne(serialNumber)).contains(processLogA);
    }

    @Test
    public void 완료시간() {

        ProcessLog processLogA = processLogService.createProcessLog(productService.createProduct(), 1);

        processLogService.updateProcessResult(processLogA, ResultState.OK);

        Assertions.assertThat(processLogA.getCompletedAt()).isNotNull();
        Assertions.assertThat(processLogA.getResult()).isEqualTo(ResultState.OK);
    }

    @Test
    public void 공정_결과_조회() {
        ProcessLog processLogA = processLogService.createProcessLog(productService.createProduct(), 1);
        processLogService.createProcessLog(processLogA.getProduct(), 2);
        processLogService.createProcessLog(processLogA.getProduct(), 3);
        processLogService.createProcessLog(processLogA.getProduct(), 4);

        ProcessLog processLogB = processLogService.createProcessLog(productService.createProduct(), 1);
        ProcessLog processLogC = processLogService.createProcessLog(productService.createProduct(), 1);

        processLogService.updateProcessResult(processLogA, ResultState.NG);
        processLogService.updateProcessResult(processLogB, ResultState.NG);
        processLogService.updateProcessResult(processLogC, ResultState.NG);

        List<ProcessLog> list = processLogService.findProcess(1, ResultState.NG);

        Assertions.assertThat(list.size()).isEqualTo(3);
    }
}