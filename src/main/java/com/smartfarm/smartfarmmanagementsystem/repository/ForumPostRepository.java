package com.smartfarm.smartfarmmanagementsystem.repository;

import com.smartfarm.smartfarmmanagementsystem.entity.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

    // Forum konularını en yeniden en eskiye doğru sıralayarak getirmemizi sağlayacak özel metod:
    List<ForumPost> findAllByOrderByCreatedAtDesc();

}