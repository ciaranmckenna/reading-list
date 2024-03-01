package com.ciaranmckenna.readinglistapp.dao.repository;

import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);
    List<Category> findByNameContainingIgnoreCase(String name);

}
