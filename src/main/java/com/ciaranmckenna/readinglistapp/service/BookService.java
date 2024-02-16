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

    BookRecord findBookRecordById(Long id) throws NotFoundException;

    Optional<Book> findBookById(Long id) throws NotFoundException;

    List<BookRecord> findByTitleContainingIgnoreCase(String title);

    BookRecord getBookDetails(Long id) throws NotFoundException;

    Book addBook(Book book);

    Book saveUpdatedBook(Book book);

    Book checkWhichBookToBeSaved(Book book) throws NotFoundException;

    void deleteBookById(Long id);
    Optional<Book> updateBookById(Long id, Book book);
}
