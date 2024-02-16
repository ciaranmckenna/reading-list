package com.ciaranmckenna.readinglistapp.dao.repository;

import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitleContainingIgnoreCase(String title);
    Optional<Book> findById(Long id);
}
