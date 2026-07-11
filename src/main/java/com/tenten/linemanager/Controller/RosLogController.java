package com.tenten.linemanager.Controller;

import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import com.tenten.linemanager.service.RosLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RosLogController {

    private final RosLogService rosLogService;

    @GetMapping("/history/ros")
    public String historyRos(@RequestParam(required = false) String serialNumber,
                             @RequestParam(required = false) ResultState result,
                             Model model) {


        //allRosLogs rosOkCount rosNgCount

        List<RosLog> allRosLogs = rosLogService.findAll();
        List<RosLog> rosLogs = rosLogService.findByCriteria(serialNumber, result);

        long rosOkCount = allRosLogs
                        .stream()
                        .filter(p -> p.getOperatorDecision().equals(ResultState.OK))
                        .count();

        long rosNgCount = allRosLogs
                .stream()
                .filter(p -> p.getOperatorDecision().equals(ResultState.NG))
                .count();

        model.addAttribute("allRosLogs", allRosLogs);
        model.addAttribute("rosLogs", rosLogs);
        model.addAttribute("rosOkCount", rosOkCount);
        model.addAttribute("rosNgCount", rosNgCount);

        return "ros-history";
    }
}
