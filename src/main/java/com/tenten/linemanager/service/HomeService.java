package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import com.tenten.linemanager.dto.HomeDto;
import com.tenten.linemanager.dto.ProductStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {


    public List<Product> recentDone(List<Product> doneProducts) {

        return doneProducts.stream()
                .filter(p -> p.getCompletedAt() != null && p.getCompletedAt()
                        .isAfter(LocalDateTime.now().minusSeconds(5))).toList();
    }

    public List<Product> recentDoneList(List<Product> doneProducts) {

        return doneProducts.stream()
                .filter(p -> p.getCompletedAt() != null)
                .sorted(Comparator.comparing(Product::getCompletedAt).reversed())
                .limit(10)
                .toList();
    }

    public void allRos(List<RosLog> rosLogs, ProductStatusDto dto) {
        rosLogs.stream()
                .filter(r -> r.getProduct().getSerialNumber().equals(dto.getSerialNumber()))
                .findFirst()
                .ifPresent(r -> dto.setRosDecision(r.getOperatorDecision()));
    }


}
