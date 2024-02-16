package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AuthorService {

    List<AuthorRecord> findAllAuthors();

    List<Author> findAll();

    Author findAuthorById(Long id) throws NotFoundException;

    List<AuthorRecord> findByAuthorNameContainingIgnoreCase(String firstName, String lastName);

    Optional<Author> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    Author addAuthor(Author author);

    Author saveNewAuthor(Author author);


    void deleteAuthorById(Long id);
}
