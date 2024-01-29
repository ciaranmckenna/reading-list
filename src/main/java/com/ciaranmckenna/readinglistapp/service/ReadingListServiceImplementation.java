package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.repository.AuthorRepository;
import com.ciaranmckenna.readinglistapp.dao.repository.BookRepository;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        return authorList.stream()
                .map(author -> new AuthorRecord(author.getId(), author.getFirstName(), author.getLastName())).toList();
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
    public Author addAuthor(String firstName, String lastName) {

        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        return authorRepository.save(author);

    }

}
