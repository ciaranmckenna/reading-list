package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {

    List<BookRecord> findAllBooks();

    BookRecord findBookById(int id) throws NotFoundException;

    List<BookRecord> findByTitleContainingIgnoreCase(String title);

    BookRecord getBookDetails(int id) throws NotFoundException;

    Book addBook(Book book);

    void deleteBookById(int id);
}
