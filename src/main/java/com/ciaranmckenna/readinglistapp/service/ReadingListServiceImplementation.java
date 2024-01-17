package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.AuthorDetail;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.repository.AuthorRepository;
import com.ciaranmckenna.readinglistapp.dao.repository.BookRepository;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.model.AuthorModel;
import com.ciaranmckenna.readinglistapp.model.BookModel;
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
    public List<BookModel> findAllBooks(){

        List<Book> bookList = bookRepository.findAll();

        List<BookModel> bookModelList = new ArrayList<>();

        for (Book book: bookList) {
            BookModel bookModel = new BookModel(book.getTitle(), book.getAuthor().getFirstName(), book.getAuthor().getLastName());
            bookModelList.add(bookModel);
        }
        return bookModelList;
    }

    @Override
    public Book findById(int id) throws NotFoundException {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return optionalBook.orElseThrow(() -> new NotFoundException("No book found"));
    }

    @Override
    public BookModel getBookDetails(int id) throws NotFoundException {
        Book book = findById(id);

        if (book!=null) {
            return new BookModel(
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

//        Book book = new Book();
//        book.setTitle(title);
//
//        //Author author = new Author(authorFirstName, authorLastName);
//        Author author = new Author();
//
//        book.setAuthor(author);
//
//        return bookRepository.save(book);

        return bookRepository.save(book);
    }

    @Override
    public List<AuthorModel> findAllAuthors() {
        List<Author> authorList = authorRepository.findAll();
        AuthorDetail authorDetail = new AuthorDetail();

        return authorList.stream()
                .map(author -> new AuthorModel(author.getFirstName(), author.getLastName())).toList();
    }

    @Override
    public Author addAuthor(String firstName, String lastName, String citizenship) {

        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        AuthorDetail authorDetail = new AuthorDetail();
        authorDetail.setCitizenship(citizenship);

        // associate the objects
        //author.setAuthorDetail(authorDetail);

        return authorRepository.save(author);

    }

}
