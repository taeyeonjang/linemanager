package com.tenten.linemanager.dto;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductStatusDto {

    private String serialNumber;
    private int currentProcessNo;
    private ResultState finalResult;
    private List<ProcessLog> logs;
    @Setter
    public ResultState rosDecision;

    public ResultState getResultForProcess(int processNo) {
        return logs.stream()
                .filter(log -> log.getProcessNo() == processNo)
                .findFirst()
                .map(ProcessLog::getResult)
                .orElse(null);
    }

    public static ProductStatusDto from(Product product, List<ProcessLog> processLogs) {
        ProductStatusDto dto = new ProductStatusDto();
        dto.serialNumber = product.getSerialNumber();
        dto.currentProcessNo = product.getCurrentProcessNo();
        dto.finalResult = product.getFinalResult();
        dto.logs = processLogs;
        dto.rosDecision = ResultState.INIT;

        return dto;
    }
}
