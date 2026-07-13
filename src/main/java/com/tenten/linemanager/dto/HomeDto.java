package com.tenten.linemanager.dto;

import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class HomeDto {

    private long totalDone;
    private long okCount;
    private long ngCount;
    private String defectRate;

    public static HomeDto from(List<Product> doneProducts) {

        HomeDto dto = new HomeDto();

        dto.totalDone = doneProducts.size();
        dto.okCount = doneProducts.stream().filter(p -> p.getFinalResult() == ResultState.OK).count();
        dto.ngCount = doneProducts.stream().filter(p -> p.getFinalResult() == ResultState.NG).count();
        dto.defectRate = dto.totalDone == 0 ? "0.0"
                : String.format("%.1f", (dto.ngCount * 100.0 / dto.totalDone));

        return dto;
    }

}
