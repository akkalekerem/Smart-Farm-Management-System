package com.smartfarm.smartfarmmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.smartfarm.smartfarmmanagementsystem.entity.Notification;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.NotificationRepository;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import org.springframework.security.core.Authentication;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    @GetMapping("/notifications")
    public String notifications(Model model, Authentication authentication) {
        List<Notification> userNotifications = new ArrayList<>();

        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = userService.findByEmail(authentication.getName());
            if (currentUser != null) {
                // Veritabanından çekiyoruz
                userNotifications = notificationRepository.findByUserOrderByTimestampDesc(currentUser);
            }
        }

        // null hatası almamak için liste boş olsa bile mutlaka model'e ekliyoruz
        model.addAttribute("notifications", userNotifications);
        model.addAttribute("activePage", "notifications");
        return "pages/notifications";
    }

    @GetMapping("/markets")
    public String markets(Model model) {
        model.addAttribute("activePage", "markets");
        return "pages/markets";
    }


}