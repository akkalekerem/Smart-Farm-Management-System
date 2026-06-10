package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserRepository userRepository;

    @ModelAttribute("user")
    public User addUserToModel(Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);

            // EĞER VERİTABANINDA NULL KALMIŞ ALANLAR VARSA VARSAYILAN ATAYALIM
            if (user != null) {
                if (user.getDarkModeActive() == null) user.setDarkModeActive(false);
                if (user.getWantsEmailReports() == null) user.setWantsEmailReports(true);
                if (user.getTemperatureUnit() == null) user.setTemperatureUnit("Celsius");
            }
            return user;
        }
        return null;
    }
}