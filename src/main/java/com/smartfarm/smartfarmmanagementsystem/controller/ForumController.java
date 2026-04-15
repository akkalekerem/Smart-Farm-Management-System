package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.ForumPost;
import com.smartfarm.smartfarmmanagementsystem.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/forum") // Bu sınıftaki tüm adresler /forum ile başlayacak
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    // 1. Forum Anasayfası (Tüm konuları listeler)
    @GetMapping
    public String forumHome(Model model) {
        // Servisten tüm konuları çekip, Thymeleaf'in okuyabilmesi için "posts" adıyla modele ekliyoruz
        model.addAttribute("posts", forumService.getAllPosts());
        model.addAttribute("activePage", "forum");
        return "pages/forum";
    }

    // 2. Yeni Konu Açma İşlemi (Formdan gelen veriyi yakalar)
    @PostMapping("/add")
    public String createPost(@RequestParam String title,
                             @RequestParam String category,
                             @RequestParam String content,
                             Authentication authentication) {
        // Sisteme giriş yapmış olan çiftçinin e-postasını alıyoruz
        String email = authentication.getName();

        // Servis katmanına verileri gönderip konuyu kaydettiriyoruz
        forumService.createPost(title, content, category, email);

        // İşlem bitince kullanıcıyı tekrar forum anasayfasına yönlendiriyoruz
        return "redirect:/forum";
    }

    // 3. Konu Detay Sayfası (İçerik ve Yorumları gösterir)
    @GetMapping("/{id}")
    public String postDetail(@PathVariable("id") Long id, Model model) {
        ForumPost post = forumService.getPostById(id);
        if (post == null) {
            return "redirect:/forum"; // Eğer konu silinmişse veya yoksa anasayfaya at
        }
        model.addAttribute("post", post);
        model.addAttribute("activePage", "forum");
        return "pages/forum_detail"; // Bir sonraki adımda bu HTML sayfasını tasarlayacağız
    }

    // 4. Konuya Yorum Ekleme İşlemi
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable("id") Long id,
                             @RequestParam String content,
                             Authentication authentication) {
        String email = authentication.getName();
        forumService.addCommentToPost(id, content, email);

        // Yorum yapıldıktan sonra aynı konunun detay sayfasına geri dön
        return "redirect:/forum/" + id;
    }
}