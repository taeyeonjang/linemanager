package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import com.tenten.linemanager.dto.RosStatusDto;
import com.tenten.linemanager.repository.ProductRepository;
import com.tenten.linemanager.repository.RosLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class RosLogService {

    private final RosLogRepository rosLogRepository;
    private final ProductRepository productRepository;

    @Transactional
    public RosLog createRosLog(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow();

        RosLog rosLog = RosLog.create(product);

        rosLogRepository.save(rosLog);

        return rosLog;
    }

    @Transactional
    public RosLog update(Long rosLogId, ResultState result) {

        RosLog rosLog = rosLogRepository.findById(rosLogId).orElseThrow(() -> new IllegalStateException("없는 RosLog입니다. rosLogId = " + rosLogId));

        rosLog.update(result);

        return rosLog;
    }

    public Optional<RosLog> findOne(String serialNumber) {
        return rosLogRepository.findByProductSerialNumber(serialNumber);
    }

    public List<RosLog> findByState(ResultState state) {
        return rosLogRepository.findOperatorDecision(state);
    }

    public List<RosLog> findAll() {
        return rosLogRepository.findAll();
    }

    public List<RosLog> findByCriteria(String serialNumber, ResultState result) {
        return rosLogRepository.findByCriteria(serialNumber, result);
    }

    public RosStatusDto findAllDto() {

        List<RosLog> allRosLogs = rosLogRepository.findAll();

        long rosOkCount = allRosLogs
                .stream()
                .filter(p -> p.getOperatorDecision().equals(ResultState.OK))
                .count();

        long rosNgCount = allRosLogs
                .stream()
                .filter(p -> p.getOperatorDecision().equals(ResultState.NG))
                .count();

        return new RosStatusDto(allRosLogs, rosOkCount, rosNgCount);
    }
}
