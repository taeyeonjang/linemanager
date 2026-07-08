package com.tenten.linemanager.dto;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.ResultState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
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
}
