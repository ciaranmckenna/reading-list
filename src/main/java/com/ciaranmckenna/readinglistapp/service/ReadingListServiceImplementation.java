package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dao.repository.AuthorRepository;
import com.ciaranmckenna.readinglistapp.dao.repository.BookRepository;
import com.ciaranmckenna.readinglistapp.dao.repository.CategoryRepository;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReadingListServiceImplementation implements ReadingListService{

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    public ReadingListServiceImplementation(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<BookRecord> findAllBooks(){

        List<Book> bookList = bookRepository.findAll();

        List<BookRecord> bookRecordList = new ArrayList<>();

        for (Book book: bookList) {
            String categoryName = (book.getCategory() != null) ? book.getCategory().getName() : "";
            BookRecord bookRecord = new BookRecord(book.getId(), book.getTitle(), book.getAuthor().getFirstName(), book.getAuthor().getLastName(), categoryName);
            bookRecordList.add(bookRecord);
        }

        Collections.sort(bookRecordList, Comparator.comparing(BookRecord::title));
        return bookRecordList;
    }

    @Override
    public Book findBookById(int id) throws NotFoundException {
        Optional<Book> optionalBookId = bookRepository.findById(id);
        return optionalBookId.orElseThrow(() -> new NotFoundException("No book id found"));
    }

    @Override
    public List<BookRecord> findByTitleContainingIgnoreCase(String title) {
        List<Book> bookListByTitleLike = bookRepository.findByTitleContainingIgnoreCase(title);
        List<BookRecord> bookRecordList = new ArrayList<>();

        for (Book book : bookListByTitleLike) {
            String categoryName = (book.getCategory() != null) ? book.getCategory().getName() : "";
            BookRecord bookRecord = new BookRecord(book.getId(), book.getTitle(),book.getAuthor().getFirstName(), book.getAuthor().getLastName(), categoryName);
            bookRecordList.add(bookRecord);
        }
        return bookRecordList;
    }

    @Override
    public BookRecord getBookDetails(int id) throws NotFoundException {
        Book book = findBookById(id);

        if (book!=null) {
            String categoryName = (book.getCategory() != null) ? book.getCategory().getName() : "";
            return new BookRecord(

                    book.getId(),
                    book.getTitle(),
                    book.getAuthor().getFirstName(),
                    book.getAuthor().getLastName(),
                    categoryName
            );
        } else {
            return null;
        }
    }

    @Override
    public Book addBook(Book book) {
        List<Book> listTitleContaining = bookRepository.findByTitleContainingIgnoreCase(book.getTitle());

        if (!listTitleContaining.isEmpty()){
            return listTitleContaining.get(0);
        }else {
        return bookRepository.save(book);
        }

    }

    @Override
    public void deleteBookById(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<AuthorRecord> findAllAuthors() {
        List<Author> authorList = authorRepository.findAll();

        List<AuthorRecord> authorRecordList = new ArrayList<>();

        for(Author author : authorList){
            AuthorRecord authorRecord = new AuthorRecord(author.getId(), author.getFirstName(), author.getLastName());
            authorRecordList.add(authorRecord);
        }
        Collections.sort(authorRecordList, Comparator.comparing(AuthorRecord::lastName, Comparator.nullsLast(String::compareTo)));
        return authorRecordList;
    }

    @Override
    public Author findAuthorById(int id) throws NotFoundException {
        Optional<Author> optionalAuthorId = authorRepository.findById(id);
        return optionalAuthorId.orElseThrow(() -> new NotFoundException("No author id found"));
    }

    @Override
    public List<AuthorRecord> findByAuthorNameContainingIgnoreCase(String firstName, String lastName ) {
        List<Author> authorNameList = authorRepository.findByFirstNameOrLastName(firstName, lastName);

        List<AuthorRecord> authorRecordList = new ArrayList<>();

        for (Author author : authorNameList) {
            AuthorRecord authorRecord = new AuthorRecord(author.getId(), author.getFirstName(), author.getLastName());
            authorRecordList.add(authorRecord);
        }
        return authorRecordList;
    }

    @Override
    public Author addAuthor(Author author) {
        List<Author> authorNameContainingList = authorRepository.findByFirstNameOrLastName(author.getFirstName(), author.getLastName());

        if (!authorNameContainingList.isEmpty()){
            return authorNameContainingList.get(0);
        }else {
        return authorRepository.save(author);
        }
    }

    @Override
    public void deleteAuthorById(int id){
        authorRepository.deleteById(id);
    }

    @Override
    public Category findCategoryById(int id) throws NotFoundException {
        Optional<Category> categoryById = categoryRepository.findById(id);
        return categoryById.orElseThrow(() -> new NotFoundException("No category id found"));
    }

}
