package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    List<BookRecord> findAllBooks();

    BookRecord findBookById(Long id) throws NotFoundException;

    List<BookRecord> findByTitleContainingIgnoreCase(String title);

    BookRecord getBookDetails(Long id) throws NotFoundException;

    Book addBook(Book book);

    void deleteBookById(Long id);
    Optional<BookRecord> updateBookById(Long id, Book book);
}
