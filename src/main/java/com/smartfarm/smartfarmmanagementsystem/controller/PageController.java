package com.smartfarm.smartfarmmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("activePage", "home");
        return "pages/index";
    }

//    @GetMapping("/forum")
//    public String forum(Model model) {
//        model.addAttribute("activePage", "forum");
//        return "pages/forum";
//    }



    @GetMapping("/markets")
    public String markets(Model model) {
        model.addAttribute("activePage", "markets");
        return "pages/markets";
    }

    @GetMapping("/notifications")
    public String notifications(Model model) {
        model.addAttribute("activePage", "notifications");
        return "pages/notifications";
    }
}