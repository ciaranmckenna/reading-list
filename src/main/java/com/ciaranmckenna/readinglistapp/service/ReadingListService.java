package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.model.AuthorModel;
import com.ciaranmckenna.readinglistapp.model.BookModel;

import java.util.List;

public interface ReadingListService {

    List<BookModel> findAllBooks();

    Book findById(int id) throws NotFoundException;

    BookModel getBookDetails(int id) throws NotFoundException;

    Book addBook(Book book);

    List<AuthorModel> findAllAuthors();

    Author addAuthor(String firstName, String lastName, String citizenship);
}
