package com.ciaranmckenna.readinglistapp.util;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;

public class Mapper {

    public static AuthorRecord mapAuthorEntityToAuthorRecord(Author author) {
        return new AuthorRecord(author.getId(), author.getFirstName(), author.getLastName());
    }

    public static BookRecord mapBookEntityToBookRecord(Book book) {
        return new BookRecord(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getFirstName(),
                book.getAuthor().getLastName()
        );
    }
    private Mapper() {

    }
}
