package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class LineSimulationService {

    private final ProcessLogService processLogService;
    private final ProductService productService;
    private final RosLogService rosLogService;

    private final Queue<Product> waitingQueue = new LinkedList<>();

    private boolean autoRunning = false;

    public void prepareProduct() {
        Product product = productService.createProduct();
        waitingQueue.add(product);
    }

    @Async
    public void autoStart() throws InterruptedException {
        if(waitingQueue.isEmpty()) {
            Thread.sleep(1);
        } else {
            while(autoRunning){
                Product product = waitingQueue.poll();
                Long productId = product.getId();

                productService.updateLineStatus(productId);
                processStep(productId, 1);
            }
        }
    }

    @Async
    public void startLine() {
        if (waitingQueue.isEmpty()) return;
        Product product = waitingQueue.poll();
        Long productId = product.getId();
        //상태를 러닝으로
        productService.updateLineStatus(productId);
        processStep(productId, 1);
    }

    public void processStep(Long productId, int processNo) {

        productService.updateCurrentProcessNo(productId, processNo);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        //ProcessLog 생성
        ProcessLog processLog = processLogService.createProcessLog(productId, processNo);

        //결과 판정
        ResultState result = randomResult();

        //ProcessLog, ProductCurrentResult update
        processLogService.updateProcessResult(processLog.getId(), result);


        //ROS 확인
        if (processNo == 3 && result == ResultState.NG) {
            rosLogService.createRosLog(productId);
            return;
        }

        //다음 공정 호출 (재귀)
        if (result == ResultState.OK && processNo < 5) {
            processStep(productId, processNo + 1);
        } else {
            productService.updateFinalResult(productId, result);
        }
    }

    //ROS 수동 NG 전용 메서드
    public void rosNgProcess(Long productId) {
        productService.updateFinalResult(productId, ResultState.NG);
    }

    @Async
    public void rosPopup(Long rosLogId, ResultState operatorDecision) {
        RosLog rosLog = rosLogService.update(rosLogId, operatorDecision);

        if (operatorDecision == ResultState.OK) {
            processStep(rosLog.getProduct().getId(), 4);
        } else {
            rosNgProcess(rosLog.getProduct().getId());
        }
    }

    private ResultState randomResult() {
        Random random = new Random();

        int n = random.nextInt(10);

        if(n < 9) {
            return ResultState.OK;
        } else {
            return ResultState.NG;
        }
    }
}
