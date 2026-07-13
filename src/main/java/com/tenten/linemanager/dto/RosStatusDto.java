package com.tenten.linemanager.dto;


import com.tenten.linemanager.domain.RosLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RosStatusDto {
    private List<RosLog> allRosLogs;
    private long okCount;
    private long ngCount;
}
