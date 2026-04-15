package com.smartfarm.smartfarmmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "forum_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Konu Başlığı

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // Konu İçeriği (Uzun metin olabileceği için TEXT)

    private String category; // Örn: "Sulama Teknikleri", "Zararlılarla Mücadele"

    @CreationTimestamp // Kayıt eklendiğinde o anki zamanı otomatik atar
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Bir konuyu bir kullanıcı açar (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    // Bir konunun birden fazla yorumu olabilir (One-to-Many)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumComment> comments;
}