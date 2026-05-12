package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.repository.CropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MarketController {

    private final CropRepository cropRepository;

    @GetMapping("/markets")
    public String showCropsMarketPage(Model model) {
        // Veritabanındaki tüm ürünleri çekip sayfaya gönderiyoruz
        model.addAttribute("crops", cropRepository.findAll());
        model.addAttribute("activePage", "markets");
        return "pages/markets";
    }
}