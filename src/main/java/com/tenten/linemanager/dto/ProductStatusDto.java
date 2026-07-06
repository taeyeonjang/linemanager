package com.tenten.linemanager.dto;

import com.tenten.linemanager.domain.ResultState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductStatusDto {

    private String serialNumber;
    private int currentProcessNo;
    private ResultState lastResult;

    public static ProductStatusDto of(String serialNumber, int currentProcessNo, ResultState lastResult) {
        return new ProductStatusDto(serialNumber, currentProcessNo, lastResult);
    }
}
