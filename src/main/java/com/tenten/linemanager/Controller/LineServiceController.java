package com.tenten.linemanager.Controller;

import com.tenten.linemanager.domain.*;
import com.tenten.linemanager.dto.ProductStatusDto;
import com.tenten.linemanager.service.LineSimulationService;
import com.tenten.linemanager.service.ProcessLogService;
import com.tenten.linemanager.service.ProductService;
import com.tenten.linemanager.service.RosLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LineServiceController {

    private final LineSimulationService lineSimulationService;
    private final ProductService productService;
    private final ProcessLogService processLogService;
    private final RosLogService rosLogService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> waitingProducts = productService.findByState(LineStatus.WAITING);
        List<Product> runningProducts = productService.findByState(LineStatus.RUNNING);
        List<RosLog> pendingRos = rosLogService.findByState(ResultState.INIT);

        List<ProductStatusDto> runningStatus = new ArrayList<>(); //메인하면 공정 흐름 나타내기 위한 리스트
        List<ProductStatusDto> doneStatus = new ArrayList<>(); //제품이 완료되는 시점에 공정흐름이 바로 끝나기때문에 추가한 완료 리스트


        for (Product product : runningProducts) {
            List<ProcessLog> logs = processLogService.findOne(product.getSerialNumber());
            runningStatus.add(new ProductStatusDto(product.getSerialNumber(), product.getCurrentProcessNo(), logs, product.getFinalResult()));
        }


        List<Product> recentDone = productService.findAll().stream()
                        .filter(p -> p.getStatus() == LineStatus.DONE)
                        .filter(p -> p.getCompletedAt().isAfter(LocalDateTime.now().minusSeconds(10)))
                        .toList();

        for (Product product : recentDone) {
            List<ProcessLog> logs = processLogService.findOne(product.getSerialNumber());
            doneStatus.add(new ProductStatusDto(product.getSerialNumber(), product.getCurrentProcessNo(), logs, product.getFinalResult()));
        }


        model.addAttribute("waitingProducts", waitingProducts);
        model.addAttribute("runningStatus", runningStatus);
        model.addAttribute("pendingRos", pendingRos);
        model.addAttribute("doneStatus", doneStatus);

        return "home";
    }


    @PostMapping("/prepare")
    public String prepare() {
        lineSimulationService.prepareProduct();
        return "redirect:/";
    }

    @PostMapping("/start")
    public String start() throws InterruptedException {
        lineSimulationService.startLine();
        Thread.sleep(500); // 0.5초 대기
        return "redirect:/";
    }

    @PostMapping("/ros")
    public String ros(@RequestParam Long rosLogId, @RequestParam String decision) throws InterruptedException {
        ResultState result = ResultState.valueOf(decision);
        lineSimulationService.rosPopup(rosLogId, result);
        return "redirect:/";
    }


}
