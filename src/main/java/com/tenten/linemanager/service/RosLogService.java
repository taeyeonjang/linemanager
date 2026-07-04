package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
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

    @Transactional
    public RosLog createRosLog(Product product) {

        RosLog rosLog = RosLog.create(product);

        rosLogRepository.save(rosLog);

        return rosLog;
    }

    @Transactional
    public void update(RosLog rosLog, ResultState result) {
        rosLog.update(result);
    }

    public Optional<RosLog> findOne(String serialNumber) {
        return rosLogRepository.findByProductSerialNumber(serialNumber);
    }

    public List<RosLog> findByState(ResultState state) {
        return rosLogRepository.findOperatorDecision(state);
    }
}
