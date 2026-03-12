package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    // loginden index html dosyasına geçişi sağlar.
    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            // Giriş yapan kullanıcının e-postasını kullanarak veritabanından bilgilerini getiriyoruz
            userService.findByEmail(principal.getName()).ifPresent(user -> {
                model.addAttribute("currentUser", user);
            });
        }
        return "index"; // templates/index.html
    }
    // Login sayfasını gösterir
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Register sayfasını gösterir
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Kayıt formundan gelen veriyi veritabanına kaydeder
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/login?success"; // Kayıt başarılıysa login'e yönlendir
    }

}