package com.tenten.linemanager.service;

import com.tenten.linemanager.repository.RosLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RosLogService {

    private final RosLogRepository rosLogRepository;


}
