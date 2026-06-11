package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.Ticket;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.DeviceRepository;
import com.smartfarm.smartfarmmanagementsystem.repository.FieldRepository;
import com.smartfarm.smartfarmmanagementsystem.repository.TicketRepository;
import com.smartfarm.smartfarmmanagementsystem.repository.UserRepository;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    private final FieldRepository fieldRepository;
    private final DeviceRepository deviceRepository;

    // ==========================================
    // PROFİL SAYFASI (GET)
    // ==========================================
    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        // Giriş yapan kullanıcıyı buluyoruz
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Kullanıcının sistemdeki tarla ve cihaz sayısını hesaplıyoruz
        long fieldCount = fieldRepository.countByOwner(user);
        long deviceCount = deviceRepository.countByOwner(user);

        model.addAttribute("user", user); // Kullanıcı bilgileri (isim, e-posta, rol vb.)
        model.addAttribute("fieldCount", fieldCount);
        model.addAttribute("deviceCount", deviceCount);
        model.addAttribute("activePage", "profile");

        return "user/profile";
    }

    // PROFİL GÜNCELLEME (POST)
    @PostMapping("/user/profile/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Sadece isim ve soyisim güncelleniyor
        user.setFirstName(firstName);
        user.setLastName(lastName);

        userRepository.save(user);

        return "redirect:/profile?success=profileUpdated";
    }


    // HESABI KALICI OLARAK SİL (POST)
    @PostMapping("/user/profile/delete")
    public String deleteAccount(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // UserService üzerinden kullanıcıyı ve ona bağlı tüm verileri kalıcı olarak siler
        userService.deleteUser(user.getId());

        // Kullanıcının mevcut oturumunu sonlandırır (logout)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // Başarılı silme mesajıyla login ekranına yönlendirir
        return "redirect:/login?deleted=true";
    }


    // DESTEK TALEBİ SAYFASI (GET)
    @GetMapping("/support")
    public String supportPage(Model model) {
        model.addAttribute("activePage", "support");
        return "user/user_support";
    }


    // DESTEK TALEBİ GÖNDERME (POST)
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
}
