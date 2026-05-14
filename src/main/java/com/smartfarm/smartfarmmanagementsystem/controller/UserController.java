package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.Ticket;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.TicketRepository;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TicketRepository ticketRepository; // Ticket'ları kaydetmek için ekledik

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("activePage", "profile");
        return "user/profile";
    }

    @GetMapping("/settings")
    public String settingsPage(Model model) {
        model.addAttribute("activePage", "settings");
        return "user/settings";
    }

    // ==========================================
    // DESTEK TALEBİ SAYFASI (GET)
    // ==========================================
    @GetMapping("/support")
    public String supportPage(Model model) {
        model.addAttribute("activePage", "support");
        return "user/user_support"; // templates içindeki dosya adın
    }

    // ==========================================
    // DESTEK TALEBİ GÖNDERME (POST)
    // ==========================================
    @PostMapping("/support/send")
    public String sendTicket(@RequestParam String subject,
                             @RequestParam String message,
                             Authentication authentication) {

        // Giriş yapan kullanıcının mail adresini alıp sistemden kendisini buluyoruz
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);

        // Yeni bir Ticket nesnesi oluşturup dolduruyoruz
        Ticket ticket = new Ticket();
        ticket.setSubject(subject);
        ticket.setMessage(message);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setSender(currentUser);
        ticket.setResolved(false);

        // Veritabanına kaydet
        ticketRepository.save(ticket);

        // Başarılı uyarısı için tekrar sayfaya yönlendir
        return "redirect:/support?success";
    }
}