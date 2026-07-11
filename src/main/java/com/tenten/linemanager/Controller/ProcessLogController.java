package com.tenten.linemanager.Controller;

import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.service.ProcessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProcessLogController {

    private final ProcessLogService processLogService;


    @GetMapping("/history/process")
    public String historyProcess(
        @RequestParam(required = false) String serialNumber,
        @RequestParam(required = false) Integer processNo,
        @RequestParam(required = false) ResultState result,
        Model model)
    {
        List<ProcessLog> processLogs = processLogService.findByCriteria(serialNumber, processNo, result);

        model.addAttribute("processLogs", processLogs);
        return "process-history";
    }

}
