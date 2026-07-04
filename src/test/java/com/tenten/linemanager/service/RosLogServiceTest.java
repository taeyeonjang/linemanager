package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RosLogServiceTest {
    @Autowired
    ProductService productService;
    @Autowired
    RosLogService rosLogService;

    @Test
    public void 생성() {
        RosLog rosLog = createRosLogWithProduct();

        String serialNumber = rosLog.getProduct().getSerialNumber();

        Assertions.assertThat(rosLogService.findOne(serialNumber).get()).isEqualTo(rosLog);

        Assertions.assertThat(rosLog.getCompletedAt()).isNull();
    }

    @Test
    public void ROS결과_업데이트() {
        RosLog rosLog = createRosLogWithProduct();

        rosLogService.update(rosLog, ResultState.NG);

        Assertions.assertThat(rosLog.getOperatorDecision()).isEqualTo(ResultState.NG);
    }

    @Test
    public void ROS결과_조회() {

        RosLog rosLogA = createRosLogWithProduct();

        rosLogService.update(rosLogA, ResultState.OK);

        RosLog rosLogB= createRosLogWithProduct();

        rosLogService.update(rosLogB, ResultState.NG);

        RosLog rosLogC = createRosLogWithProduct();

        rosLogService.update(rosLogC, ResultState.NG);

        List<RosLog> list = rosLogService.findByState(ResultState.NG);

        Assertions.assertThat(rosLogService.findByState(ResultState.NG)).contains(rosLogB);
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

    private RosLog createRosLogWithProduct() {
        return rosLogService.createRosLog(productService.createProduct());
    }
}