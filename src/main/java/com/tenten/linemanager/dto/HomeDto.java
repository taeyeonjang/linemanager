package com.tenten.linemanager.dto;

import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.RosLog;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter @Setter
public class HomeDto {

    private long totalDone;
    private long okCount;
    private long ngCount;
    private String defectRate;
    private boolean autoRunning;

    private List<Product> waitingProducts;
    private List<Product> runningProducts;
    private List<Product> doneProducts;
    private List<RosLog> pendingRos;
    private List<RosLog> allRos;


    private List<ProductStatusDto> runningStatus;
    private List<ProductStatusDto> doneStatus;
    private List<ProductStatusDto> allStatus;
    private List<Product> recentDoneList;

}
