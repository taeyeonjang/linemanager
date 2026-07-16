package com.tenten.linemanager.Controller;

import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.domain.RosLog;
import com.tenten.linemanager.dto.PageDto;
import com.tenten.linemanager.dto.RosStatusDto;
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
                             @RequestParam(defaultValue = "1") int page,
                             Model model) {


        //allRosLogs rosOkCount rosNgCount

        RosStatusDto rosLogsDto = rosLogService.findAllDto();
        PageDto<RosLog> queryDslRosLogs = rosLogService.findByQueryDsl(serialNumber, result, page);



        model.addAttribute("rosLogsDto", rosLogsDto);
        model.addAttribute("queryDslRosLogs", queryDslRosLogs);

        return "ros-history";
    }
}
