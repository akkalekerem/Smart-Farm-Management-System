package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/markets")
    public String markets(Model model) {
        model.addAttribute("prices", marketService.getMarketPrices());
        model.addAttribute("activePage", "markets");
        return "pages/markets";
    }
}