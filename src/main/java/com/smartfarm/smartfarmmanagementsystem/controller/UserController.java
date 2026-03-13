package com.smartfarm.smartfarmmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/profile")
    public String profilePage(Model model) {
        // activePage bilgisini navbar'daki görsel işaretleme için gönderiyoruz
        model.addAttribute("activePage", "profile");
        return "user/profile";
    }

    @GetMapping("/settings")
    public String settingsPage(Model model) {
        model.addAttribute("activePage", "settings");
        return "user/settings";
    }
}