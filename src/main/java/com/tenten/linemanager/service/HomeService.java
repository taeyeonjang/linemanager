package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.*;
import com.tenten.linemanager.dto.HomeDto;
import com.tenten.linemanager.dto.ProductStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final ProductService productService;
    private final RosLogService rosLogService;
    private final ProcessLogService processLogService;
    private final LineSimulationService lineSimulationService;

    public HomeDto getHomeData() {

        HomeDto homeDto = new HomeDto();

        List<Product> waitingProducts = productService.findByState(LineStatus.WAITING);
        List<Product> runningProducts = productService.findByState(LineStatus.RUNNING);
        List<Product> doneProducts = productService.findByState(LineStatus.DONE);
        List<RosLog> pendingRos = rosLogService.findByState(ResultState.INIT);
        List<RosLog> allRos = rosLogService.findAll();
        boolean autoRunning = lineSimulationService.getAutoRunning();

        List<ProductStatusDto> runningStatus = new ArrayList<>(); //메인하면 공정 흐름 나타내기 위한 리스트
        List<ProductStatusDto> doneStatus = new ArrayList<>(); //완료제품 리스트
        List<ProductStatusDto> allStatus = new ArrayList<>();

        for (Product product : runningProducts) {
            List<ProcessLog> logs = processLogService.findOne(product.getSerialNumber());
            runningStatus.add(ProductStatusDto.from(product, logs));
        }
        List<Product> recentDone = recentDone(doneProducts);
        List<Product> recentDoneList = recentDoneList(doneProducts);

        for (Product product : recentDone) {
            List<ProcessLog> logs = processLogService.findOne(product.getSerialNumber());
            doneStatus.add(ProductStatusDto.from(product, logs));
        }

        allStatus.addAll(runningStatus);
        allStatus.addAll(doneStatus);

        for (ProductStatusDto dto : allStatus) {
            allRos(allRos, dto);
        }

        long totalDone = doneProducts.size();
        long okCount = doneProducts.stream().filter(p -> p.getFinalResult() == ResultState.OK).count();
        long ngCount = doneProducts.stream().filter(p -> p.getFinalResult() == ResultState.NG).count();
        String defectRate = totalDone == 0 ? "0.0"
                : String.format("%.1f", (ngCount * 100.0 / totalDone));

        homeDto.setWaitingProducts(waitingProducts);
        homeDto.setPendingRos(pendingRos);
        homeDto.setDoneStatus(doneStatus);
        homeDto.setRecentDoneList(recentDoneList);
        homeDto.setRunningStatus(runningStatus);
        homeDto.setTotalDone(totalDone);
        homeDto.setOkCount(okCount);
        homeDto.setNgCount(ngCount);
        homeDto.setDefectRate(defectRate);
        homeDto.setAutoRunning(autoRunning);

        return homeDto;
    }



    private List<Product> recentDone(List<Product> doneProducts) {

        return doneProducts.stream()
                .filter(p -> p.getCompletedAt() != null && p.getCompletedAt()
                        .isAfter(LocalDateTime.now().minusSeconds(5))).toList();
    }

    private List<Product> recentDoneList(List<Product> doneProducts) {

        return doneProducts.stream()
                .filter(p -> p.getCompletedAt() != null)
                .sorted(Comparator.comparing(Product::getCompletedAt).reversed())
                .limit(10)
                .toList();
    }

    private void allRos(List<RosLog> rosLogs, ProductStatusDto dto) {
        rosLogs.stream()
                .filter(r -> r.getProduct().getSerialNumber().equals(dto.getSerialNumber()))
                .findFirst()
                .ifPresent(r -> dto.setRosDecision(r.getOperatorDecision()));
    }


}
