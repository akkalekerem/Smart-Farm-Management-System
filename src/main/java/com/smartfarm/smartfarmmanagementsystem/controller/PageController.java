package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor // UserService'in otomatik bağlanmasını sağlar
public class PageController {

    private final UserService userService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        prepareModel(model, "home", authentication);
        return "pages/index";
    }

    @GetMapping("/devices")
    public String devices(Model model, Authentication authentication) {
        prepareModel(model, "devices", authentication);
        return "pages/devices";
    }

    @GetMapping("/forum")
    public String forum(Model model, Authentication authentication) {
        prepareModel(model, "forum", authentication);
        return "pages/forum";
    }

    @GetMapping("/markets")
    public String markets(Model model, Authentication authentication) {
        prepareModel(model, "markets", authentication);
        return "pages/markets";
    }

    @GetMapping("/notifications")
    public String notifications(Model model, Authentication authentication) {
        prepareModel(model, "notifications", authentication);
        return "pages/notifications";
    }

    /**
     * Ortak model verilerini hazırlar: Aktif sayfa, Admin kontrolü ve Kullanıcı adı.
     */
    private void prepareModel(Model model, String activePage, Authentication authentication) {
        model.addAttribute("activePage", activePage);

        boolean isAdmin = false;
        String displayName = "Misafir";

        if (authentication != null && authentication.isAuthenticated()) {
            // Güvenlik katmanından gelen email bilgisini alıyoruz
            String email = authentication.getName();

            // Veritabanından kullanıcının tüm bilgilerini çekiyoruz
            User user = userService.findByEmail(email);

            if (user != null) {
                // Sadece ismi (firstName) alıp ekrana gönderiyoruz
                displayName = user.getFirstName();

                // Kullanıcının ADMIN yetkisi olup olmadığını kontrol ediyoruz
                isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            }
        }

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("userName", displayName);
    }
}