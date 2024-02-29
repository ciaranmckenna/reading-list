package com.ciaranmckenna.readinglistapp.util;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Mapper {

    public static AuthorRecord mapAuthorEntityToAuthorRecord(Author author) {
        return new AuthorRecord(author.getId(), author.getFirstName(), author.getLastName());
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
        return new CategoryRecord(category.getId(), category.getName()); /// causing me to only return the first element
    }

    public static List<AuthorRecord> mapToAuthorRecordList(List<Author> authors) {
        return authors.stream()
                .map(Mapper::mapAuthorEntityToAuthorRecord)
                .collect(Collectors.toList());
    }

    public static List<CategoryRecord> mapToCategoryRecordList(List<Category> categories){
        return categories.stream()
                .map(Mapper::mapCategoryEntityToCategoryRecord)
                .collect(Collectors.toList());
    }
    private Mapper() {

    }
}
