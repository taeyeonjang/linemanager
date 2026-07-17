package com.tenten.linemanager.Controller;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.dto.PageDto;
import com.tenten.linemanager.service.ProcessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProcessLogApiController {

    private final ProcessLogService processLogService;

    @GetMapping("/api/process-logs")
    public PageDto<ProcessLog> getProcessLogs (
            @RequestParam(required = false) String serialNumber,
            @RequestParam(required = false) Integer processNo,
            @RequestParam(required = false) ResultState result,
            @RequestParam(defaultValue = "1") int page
    ) {

        return processLogService.findByQueryDsl(serialNumber, processNo, result, page);
    }


}
