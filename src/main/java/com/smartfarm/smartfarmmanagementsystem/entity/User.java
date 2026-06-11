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

    @Enumerated(EnumType.STRING)
    private Role role;

    // Bir kullanıcının birden fazla cihazı olabilir
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices;

    // Bir kullanıcının birden fazla tarlası olabilir (SİLME İŞLEMİ İÇİN ŞART)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Field> fields;

    // Kullanıcıya ait destek talepleri (SİLME İŞLEMİ İÇİN ŞART)
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    // Bir kullanıcının birden fazla forum gönderisi olabilir
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumPost> forumPosts;

    // Bir kullanıcının birden fazla forum yorumu olabilir
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumComment> forumComments;

    // Kullanıcıya ait bildirimler
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;
}
