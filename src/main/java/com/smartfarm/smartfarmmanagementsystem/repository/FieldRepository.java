package com.smartfarm.smartfarmmanagementsystem.repository;

import com.smartfarm.smartfarmmanagementsystem.entity.Field;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    // Giriş yapan çiftçinin (User) sahip olduğu tüm tarlaları liste halinde getirir
    List<Field> findByOwner(User owner);

}