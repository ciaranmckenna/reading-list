package com.ciaranmckenna.readinglistapp.util;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    public static AuthorRecord mapAuthorEntityToAuthorRecord(Author author) {
        List<String> books = author.getBooks().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
        return new AuthorRecord(author.getId(), author.getFirstName(), author.getLastName(), books);
    }

    public static BookRecord mapBookEntityToBookRecord(Book book) {
        String authorFirstName = book.getAuthor() != null ? book.getAuthor().getFirstName() : "N/A";
        String authorLastName = book.getAuthor() != null ? book.getAuthor().getLastName() : "N/A";
        String category = book.getCategory() != null ? book.getCategory().getName() : " ";
        return new BookRecord(
                book.getId(),
                book.getTitle(),
                authorFirstName,
                authorLastName,
                category
        );
    }

    public static CategoryRecord mapCategoryEntityToCategoryRecord(Category category){
        List<String> titles = category.getBooks().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());

        List<String> authorFirstNames = category.getBooks().stream()
                .map(book -> book.getAuthor().getFirstName())
                .collect(Collectors.toList());

        List<String> authorLastNames = category.getBooks().stream()
                .map(book -> book.getAuthor().getLastName())
                .collect(Collectors.toList());

        return new CategoryRecord(category.getId(), category.getName(), titles, authorFirstNames, authorLastNames);
    }

    public static List<AuthorRecord> mapToAuthorRecordList(List<Author> authors) {
        return authors.stream()
                .map(Mapper::mapAuthorEntityToAuthorRecord)
                .collect(Collectors.toList());
    }

    public static List<CategoryRecord> categoryListMappedToCategoryRecordList(List<Category> categories){
        return categories.stream()
                .map(Mapper::mapCategoryEntityToCategoryRecord)
                .collect(Collectors.toList());
    }
    private Mapper() {

    }
}
