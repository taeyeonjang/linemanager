package com.tenten.linemanager.Controller;

import com.tenten.linemanager.domain.*;
import com.tenten.linemanager.dto.HomeDto;
import com.tenten.linemanager.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LineServiceController {

    private final LineSimulationService lineSimulationService;
    private final HomeService homeService;

    @GetMapping("/")
    public String home(Model model) {
        HomeDto homeDto = homeService.getHomeData();

        model.addAttribute("homeDto", homeDto);

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

    @PostMapping("/autoStart")
    public String autoStart() throws InterruptedException {
        lineSimulationService.autoStart();
        Thread.sleep(500); // 0.5초 대기
        return "redirect:/";
    }

    @PostMapping("/autoStop")
    public String autoStop() {
        lineSimulationService.autoStop();
        return "redirect:/";
    }

    @PostMapping("/ros")
    public String ros(@RequestParam Long rosLogId, @RequestParam String decision) {
        ResultState result = ResultState.valueOf(decision);
        lineSimulationService.rosPopup(rosLogId, result);
        return "redirect:/";
    }

}
