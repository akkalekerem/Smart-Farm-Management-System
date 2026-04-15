package com.smartfarm.smartfarmmanagementsystem.service;

import com.smartfarm.smartfarmmanagementsystem.entity.ForumComment;
import com.smartfarm.smartfarmmanagementsystem.entity.ForumPost;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.ForumCommentRepository;
import com.smartfarm.smartfarmmanagementsystem.repository.ForumPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumPostRepository forumPostRepository;
    private final ForumCommentRepository forumCommentRepository;
    private final UserService userService; // İşlem yapan kullanıcıyı bulmak için

    // 1. Tüm konuları en yeniden eskiye sıralı getir
    public List<ForumPost> getAllPosts() {
        return forumPostRepository.findAllByOrderByCreatedAtDesc();
    }

    // 2. ID'sine göre tek bir konuyu getir (Detay sayfası için)
    public ForumPost getPostById(Long id) {
        return forumPostRepository.findById(id).orElse(null);
    }

    // 3. Yeni bir forum konusu oluştur
    public void createPost(String title, String content, String category, String email) {
        User author = userService.findByEmail(email);
        if (author != null) {
            ForumPost post = new ForumPost();
            post.setTitle(title);
            post.setContent(content);
            post.setCategory(category);
            post.setAuthor(author);
            forumPostRepository.save(post);
        }
    }

    // 4. Var olan bir konuya yeni bir yorum ekle
    public void addCommentToPost(Long postId, String content, String email) {
        User author = userService.findByEmail(email);
        ForumPost post = getPostById(postId);

        if (author != null && post != null) {
            ForumComment comment = new ForumComment();
            comment.setContent(content);
            comment.setAuthor(author);
            comment.setPost(post);
            forumCommentRepository.save(comment);
        }
    }
}