package com.tenten.linemanager.Controller;

import com.tenten.linemanager.domain.LineStatus;
import com.tenten.linemanager.domain.ProcessLog;
import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.dto.ProductStatusDto;
import com.tenten.linemanager.service.LineSimulationService;
import com.tenten.linemanager.service.ProcessLogService;
import com.tenten.linemanager.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LineServiceController {

    private final LineSimulationService lineSimulationService;
    private final ProductService productService;
    private final ProcessLogService processLogService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> waitingProducts = productService.findByState(LineStatus.WAITING);
        List<Product> runningProducts = productService.findByState(LineStatus.RUNNING);

        List<ProductStatusDto> runningStatus = new ArrayList<>();


        for (Product product : runningProducts) {
            List<ProcessLog> logs = processLogService.findOne(product.getSerialNumber());
            ResultState result = logs.stream()
                    .filter(log -> log.getProcessNo() == product.getCurrentProcessNo() - 1)
                    .findFirst()
                    .map(ProcessLog::getResult)
                    .orElse(ResultState.INIT);

            runningStatus.add(ProductStatusDto.of(product.getSerialNumber(), product.getCurrentProcessNo(), result));
        }

        model.addAttribute("waitingProducts", waitingProducts);
        model.addAttribute("runningStatus", runningStatus);

        return "home";
    }


    @PostMapping("/prepare")
    public String prepare() {
        lineSimulationService.prepareProduct();
        return "redirect:/";
    }

    @PostMapping("/start")
    public String start() {
        lineSimulationService.startLine();
        return "redirect:/";
    }


}
