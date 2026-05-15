package com.smartfarm.smartfarmmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'nin otomatik artmasını sağlar
    private Long id;

    @Column(unique = true, nullable = false) // Aynı e-posta ile iki kez kayıt olunmasını engeller
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "city")
    private String city;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Bir kullanıcının birden fazla cihazı olabilir (One-To-Many)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Device> devices;

    // Bir kullanıcının birden fazla forum gönderisi olabilir
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<ForumPost> forumPosts;

    // Bir kullanıcının birden fazla forum yorumu olabilir
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<ForumComment> forumComments;

    @Column(name = "temperature_unit")
    private String temperatureUnit = "Celsius"; // Varsayılan değer Celsius olsun

    @Column(name = "wants_email_reports")
    private Boolean wantsEmailReports = true; // Varsayılan olarak bildirimler açık olsun

    @Column(name = "dark_mode_active")
    private Boolean darkModeActive = false; // Varsayılan olarak açık tema (karanlık mod kapalı)

}
