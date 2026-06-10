package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Login sayfasını gösterir
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    // Register sayfasını gösterir
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("firstName") String firstName,
                               @RequestParam("lastName") String lastName,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password) {

        // Konsolda verilerin düşüp düşmediğini anlık izleyelim:
        System.out.println("=== PARAMETRİK KAYIT İSTEĞİ BAŞARILI ===");
        System.out.println("Gelen E-posta: " + email);

        // Boş bir User nesnesi oluşturup formdan gelen parametreleri elle set ediyoruz
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        // Kayıt servisini tetikliyoruz
        userService.registerUser(user);

        return "redirect:/login?success";
    }
}