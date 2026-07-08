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
import java.util.Comparator;
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
        List<Product> doneProducts = productService.findByState(LineStatus.DONE);
        List<RosLog> pendingRos = rosLogService.findByState(ResultState.INIT);
        List<RosLog> allRos = rosLogService.findAll();

        long totalDone = doneProducts.size();
        long okCount = doneProducts.stream().filter(p -> p.getFinalResult() == ResultState.OK).count();
        long ngCount = doneProducts.stream().filter(p -> p.getFinalResult() == ResultState.NG).count();
        String defectRate = totalDone == 0 ? "0.0"
                : String.format("%.1f", (ngCount * 100.0 / totalDone));

        List<ProductStatusDto> runningStatus = new ArrayList<>(); //메인하면 공정 흐름 나타내기 위한 리스트
        List<ProductStatusDto> doneStatus = new ArrayList<>(); //완료제품 리스트
        List<ProductStatusDto> allStatus = new ArrayList<>();

        for (Product product : runningProducts) {
            List<ProcessLog> logs = processLogService.findOne(product.getSerialNumber());
            runningStatus.add(new ProductStatusDto(product.getSerialNumber(), product.getCurrentProcessNo(), product.getFinalResult(), logs, ResultState.INIT));
        }


        List<Product> recentDone = doneProducts.stream()
                        .filter(p -> p.getCompletedAt() != null && p.getCompletedAt().isAfter(LocalDateTime.now().minusSeconds(5)))
                        .toList();

        List<Product> recentDoneList = doneProducts.stream()
                        .filter(p -> p.getCompletedAt() != null)
                        .sorted(Comparator.comparing(Product::getCompletedAt).reversed())
                        .limit(10)
                        .toList();

        for (Product product : recentDone) {
            List<ProcessLog> logs = processLogService.findOne(product.getSerialNumber());
            doneStatus.add(new ProductStatusDto(product.getSerialNumber(), product.getCurrentProcessNo(), product.getFinalResult(), logs, ResultState.INIT));
        }

        allStatus.addAll(runningStatus);
        allStatus.addAll(doneStatus);

        for (ProductStatusDto dto : allStatus) {
            allRos.stream()
                    .filter(r -> r.getProduct().getSerialNumber().equals(dto.getSerialNumber()))
                    .findFirst()
                    .ifPresent(r -> dto.setRosDecision(r.getOperatorDecision()));
        }

        model.addAttribute("waitingProducts", waitingProducts);
        model.addAttribute("runningStatus", runningStatus);
        model.addAttribute("pendingRos", pendingRos);
        model.addAttribute("doneStatus", doneStatus);
        model.addAttribute("totalDone", totalDone);
        model.addAttribute("okCount", okCount);
        model.addAttribute("ngCount", ngCount);
        model.addAttribute("defectRate", defectRate);
        model.addAttribute("recentDoneList", recentDoneList);

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
    public String ros(@RequestParam Long rosLogId, @RequestParam String decision) {
        ResultState result = ResultState.valueOf(decision);
        lineSimulationService.rosPopup(rosLogId, result);
        return "redirect:/";
    }


}
