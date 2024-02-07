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
        String authorFirstName = book.getAuthor() != null ? book.getAuthor().getFirstName() : "N/A";
        String authorLastName = book.getAuthor() != null ? book.getAuthor().getLastName() : "N/A";
        return new BookRecord(
                book.getId(),
                book.getTitle(),
                authorFirstName,
                authorLastName
        );
    }

    private Mapper() {

    }
}
