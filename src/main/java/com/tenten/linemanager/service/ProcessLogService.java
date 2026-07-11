package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.repository.ProcessLogRepository;
import com.tenten.linemanager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProcessLogService {

    private final ProcessLogRepository processLogRepository;
    private final ProductRepository productRepository;

    public ProcessLog createProcessLog(Long productId, int processNo) {

        Product product = productRepository.findById(productId).orElseThrow();

        ProcessLog processLog = ProcessLog.create(product, processNo);

        processLogRepository.save(processLog);

        return processLog;
    }

    public void updateProcessResult(Long processLogId, ResultState result) {

        ProcessLog processLog = processLogRepository.findById(processLogId).orElseThrow(() -> new IllegalStateException("없는 ProcessLogId 입니다. processLogId = " + processLogId));

        processLog.update(result);
    }

    @Transactional(readOnly = true)
    public List<ProcessLog> findByCriteria(String serialNuber, Integer processNo, ResultState result) {
        return processLogRepository.findByCriteria(serialNuber, processNo, result);
    }

    @Transactional(readOnly = true)
    public List<ProcessLog> findAll() {
        return processLogRepository.findAll();
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
