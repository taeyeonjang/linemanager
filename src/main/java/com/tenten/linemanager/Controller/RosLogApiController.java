package com.tenten.linemanager.Controller;

import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import com.tenten.linemanager.dto.PageDto;
import com.tenten.linemanager.service.RosLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RosLogApiController {
    private final RosLogService rosLogService;

    @GetMapping("/api/ros-logs")
    public PageDto<RosLog> getRosLogs(
            @RequestParam(required = false) String serialNumber,
            @RequestParam(required = false) ResultState result,
            @RequestParam(defaultValue = "1") int page
    ) {
        return rosLogService.findByQueryDsl(serialNumber, result, page);
    }
}
