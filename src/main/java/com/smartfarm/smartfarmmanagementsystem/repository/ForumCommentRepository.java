package com.smartfarm.smartfarmmanagementsystem.repository;

import com.smartfarm.smartfarmmanagementsystem.entity.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumCommentRepository extends JpaRepository<ForumComment, Long> {

    // Sadece tıkladığımız bir konunun (post) altındaki yorumları
    // eskiden yeniye doğru sıralayarak getirmemizi sağlayacak metod:
    List<ForumComment> findByPostIdOrderByCreatedAtAsc(Long postId);

}