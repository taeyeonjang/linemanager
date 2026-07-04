package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class LineSimulationService {

    private final ProcessLogService processLogService;
    private final ProductService productService;
    private final RosLogService rosLogService;

    private final Queue<Product> watingQueue = new LinkedList<>();

    public void prepareProduct() {
        Product product = productService.createProduct();
        watingQueue.add(product);
    }

    public void startLine() {
        if (watingQueue.isEmpty()) return;
        Product product = watingQueue.poll();
        processStep(product, 1);
    }

    public void processStep(Product product, int processNo) {
        //ProcessLog 생성
        ProcessLog processLog = processLogService.createProcessLog(product, processNo);

        //결과 판정
        ResultState result = randomResult();

        //ProcessLog update
        processLogService.updateProcessResult(processLog, result);

        //ROS 확인
        if (processNo == 3 && result == ResultState.NG) {
            rosLogService.createRosLog(product);
            return;
        }

        //다음 공정 호출 (재귀)
        if (result == ResultState.OK && processNo < 5) {
            processStep(product, processNo + 1);
        } else {
            productService.updateFinalResult(product, result);
        }
    }

    public void rosPopup(RosLog rosLog, ResultState operatorDecision) {
        rosLogService.update(rosLog, operatorDecision);

        if (operatorDecision == ResultState.OK) {
            processStep(rosLog.getProduct(), 4);
        } else {
            productService.updateFinalResult(rosLog.getProduct(), operatorDecision);
        }
    }

    private ResultState randomResult() {
        Random random = new Random();

        int n = random.nextInt(10);

        if(n < 8) {
            return ResultState.OK;
        } else {
            return ResultState.NG;
        }
    }
}
