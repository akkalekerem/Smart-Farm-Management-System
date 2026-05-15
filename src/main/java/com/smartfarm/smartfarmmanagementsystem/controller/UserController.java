package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.Ticket;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.TicketRepository;
import com.smartfarm.smartfarmmanagementsystem.repository.UserRepository;
import com.smartfarm.smartfarmmanagementsystem.repository.FieldRepository; // Eklendi
import com.smartfarm.smartfarmmanagementsystem.repository.DeviceRepository; // Eklendi
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;   // İstatistik için eklendi
    private final DeviceRepository deviceRepository; // İstatistik için eklendi
    private final PasswordEncoder passwordEncoder;

    // ==========================================
    // PROFİL SAYFASI (GET) - İstatistikler Eklendi
    // ==========================================
    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        // Giriş yapan kullanıcıyı buluyoruz
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Kullanıcıya ait tarla ve cihaz sayılarını sayıyoruz
        long fieldCount = fieldRepository.countByOwner(user);
        long deviceCount = deviceRepository.countByOwner(user);

        // Verileri modele ekliyoruz
        model.addAttribute("fieldCount", fieldCount);
        model.addAttribute("deviceCount", deviceCount);
        model.addAttribute("activePage", "profile");

        return "user/profile";
    }

    @GetMapping("/settings")
    public String settingsPage(Model model) {
        model.addAttribute("activePage", "settings");
        return "user/settings";
    }

    @GetMapping("/support")
    public String supportPage(Model model) {
        model.addAttribute("activePage", "support");
        return "user/user_support";
    }

    @PostMapping("/support/send")
    public String sendTicket(@RequestParam String subject,
                             @RequestParam String message,
                             Authentication authentication) {

        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);

        Ticket ticket = new Ticket();
        ticket.setSubject(subject);
        ticket.setMessage(message);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setSender(currentUser);
        ticket.setResolved(false);

        ticketRepository.save(ticket);

        return "redirect:/support?success";
    }

    @PostMapping("/user/profile/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam(required = false) String city,
                                @RequestParam(required = false) String phoneNumber,
                                Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCity(city);
        user.setPhoneNumber(phoneNumber);

        userRepository.save(user);

        return "redirect:/profile?success";
    }

    @PostMapping("/user/settings/update")
    public String updateSettings(@RequestParam String temperatureUnit,
                                 @RequestParam(required = false) Boolean wantsEmailReports,
                                 @RequestParam(required = false) Boolean darkModeActive,
                                 Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        user.setTemperatureUnit(temperatureUnit);
        // Null kontrolü yaparak güvenli atama
        user.setWantsEmailReports(wantsEmailReports != null ? wantsEmailReports : false);
        user.setDarkModeActive(darkModeActive != null ? darkModeActive : false);

        userRepository.save(user);

        return "redirect:/settings?success";
    }

    @PostMapping("/user/profile/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "redirect:/profile?error=wrongPassword";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/profile?error=passwordMismatch";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "redirect:/profile?success=passwordChanged";
    }
}