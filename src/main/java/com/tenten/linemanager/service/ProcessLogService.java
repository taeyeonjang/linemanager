package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.repository.ProcessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProcessLogService {

    private final ProcessLogRepository processLogRepository;

    public ProcessLog createProcessLog(Product product, int processNo, ResultState result) {

        ProcessLog processLog = ProcessLog.create(product, processNo, result);

        processLogRepository.save(processLog);

        return processLog;
    }

    @Transactional
    public void updateCompltedAt(ProcessLog processLog) {
        processLog.update();
    }

    @Transactional(readOnly = true)
    public List<ProcessLog> findOne(String serialNumber) {
        return processLogRepository.findByProductSerialNumber(serialNumber);
    }

    @Transactional(readOnly = true)
    public List<ProcessLog> findProcess(int processNo, ResultState result) {
        return processLogRepository.findByProcessNoAndState(processNo, result);
    }

}
