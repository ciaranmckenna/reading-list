package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;

import java.util.List;

public interface ReadingListService {

    List<BookRecord> findAllBooks();

    Book findBookById(int id) throws NotFoundException;

    List<BookRecord> findBookByTitle(String title);

    List<BookRecord> findByTitleContainingIgnoreCase(String title);

    BookRecord getBookDetails(int id) throws NotFoundException;

    Book addBook(Book book);

    void deleteBookById(int id);

    List<AuthorRecord> findAllAuthors();

    Author findAuthorById(int id) throws NotFoundException;

    AuthorRecord getAuthorDetails(int id) throws NotFoundException;

    Author addAuthor(Author author);
}
