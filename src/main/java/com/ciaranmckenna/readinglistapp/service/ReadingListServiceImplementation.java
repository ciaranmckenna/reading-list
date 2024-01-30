package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.repository.AuthorRepository;
import com.ciaranmckenna.readinglistapp.dao.repository.BookRepository;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReadingListServiceImplementation implements ReadingListService{

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public ReadingListServiceImplementation(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<BookRecord> findAllBooks(){

        List<Book> bookList = bookRepository.findAll();

        List<BookRecord> bookRecordList = new ArrayList<>();

        for (Book book: bookList) {
            BookRecord bookRecord = new BookRecord(book.getId(), book.getTitle(), book.getAuthor().getFirstName(), book.getAuthor().getLastName());
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
    public List<BookRecord> findBookByTitle(String title) {
        List<Book> bookListTitle = bookRepository.findBytitle(title);
        List<BookRecord> bookRecordList = new ArrayList<>();

        for (Book book : bookListTitle) {
            BookRecord bookRecord = new BookRecord(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor().getFirstName(),
                    book.getAuthor().getLastName()
            );
            bookRecordList.add(bookRecord);
        }
        return bookRecordList;
    }

    @Override
    public List<BookRecord> findByTitleContainingIgnoreCase(String title) {
        List<Book> bookListByTitleLike = bookRepository.findByTitleContainingIgnoreCase(title);
        List<BookRecord> bookRecordList = new ArrayList<>();

        for (Book book : bookListByTitleLike) {
            BookRecord bookRecord = new BookRecord(book.getId(), book.getTitle(),book.getAuthor().getFirstName(), book.getAuthor().getLastName());
            bookRecordList.add(bookRecord);
        }
        return bookRecordList;
    }

    @Override
    public BookRecord getBookDetails(int id) throws NotFoundException {
        Book book = findBookById(id);

        if (book!=null) {
            return new BookRecord(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor().getFirstName(),
                    book.getAuthor().getLastName()
            );
        } else {
            return null;
        }
    }

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
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
    public AuthorRecord getAuthorDetails(int id) throws NotFoundException {
        Author author = findAuthorById(id);
        if (author != null) {
            return new AuthorRecord(
                    author.getId(),
                    author.getFirstName(),
                    author.getLastName()
            );
        } else {
            return null;
        }
    }

    @Override
    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthorById(int id){
        authorRepository.deleteById(id);
    }

}
