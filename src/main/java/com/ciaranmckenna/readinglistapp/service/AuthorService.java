package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorService {

    List<AuthorRecord> findAllAuthors();

    Author findAuthorById(int id) throws NotFoundException;

    List<AuthorRecord> findByAuthorNameContainingIgnoreCase(String firstName, String lastName);

    Author addAuthor(Author author);

    void deleteAuthorById(int id);
}
