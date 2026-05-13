package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.Notification;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.NotificationRepository;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    // Bildirimler Sayfasını Görüntüle
    @GetMapping("/notifications")
    public String showNotifications(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = userService.findByEmail(authentication.getName());

            // Kullanıcının bildirimlerini en yeniden en eskiye doğru çekiyoruz
            List<Notification> notifications = notificationRepository.findByUserOrderByTimestampDesc(currentUser);

            model.addAttribute("notifications", notifications);
        }

        model.addAttribute("activePage", "notifications");
        return "pages/notifications";
    }

    // Tekli Bildirim Silme (Opsiyonel: HTML'deki çarpı butonuna bağlayabilirsin)
    @PostMapping("/notifications/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationRepository.deleteById(id);
        return "redirect:/notifications";
    }

    // Tümünü Okundu İşaretle (Opsiyonel özellik)
    @PostMapping("/notifications/read-all")
    public String markAllAsRead(Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        List<Notification> unread = notificationRepository.findByUserAndIsReadFalse(currentUser);
        for (Notification n : unread) {
            n.setRead(true);
        }
        notificationRepository.saveAll(unread);
        return "redirect:/notifications";
    }
}