package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dao.repository.BookRepository;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;


    @Override
    public List<BookRecord> findAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(Mapper::mapBookEntityToBookRecord)
                .sorted(Comparator.comparing(BookRecord::title))
                .toList();
    }

    @Override
    public BookRecord findBookRecordById(Long id) throws NotFoundException {
        Optional<Optional<Book>> optionalBook = Optional.ofNullable(bookRepository.findById(id));
        if (optionalBook.isPresent()) {
            return Mapper.mapBookEntityToBookRecord(optionalBook.get().get());
        } else {
            String errorMessage = String.format("No book id: %s found", id);
            log.info(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public Optional<Book> findBookById(Long id) throws NotFoundException {
        return bookRepository.findById(id);
    }

    @Override
    public List<BookRecord> findByTitleContainingIgnoreCase(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(Mapper::mapBookEntityToBookRecord)
                .collect(Collectors.toList());
    }

    @Override
    public BookRecord getBookDetails(Long id) {
        Optional<Optional<Book>> currentBook = Optional.ofNullable(bookRepository.findById(id));
        return currentBook.map((Optional<Book> book) -> Mapper.mapBookEntityToBookRecord(book.get())).orElse(null);
    }

    @Override
    public Book addBook(Book book) {
        retrieveAuthorData(book);

        retrieveCategoryData(book);

        // check to see if title already exists
            Optional<Book> existingTitle = bookRepository.findByTitleContainingIgnoreCase(book.getTitle()); // fails if title is empty

            if (existingTitle.isPresent()) {
                return existingTitle.get();
            } else {
                return bookRepository.save(book);
            }
        }

    private void retrieveCategoryData(Book book) {
        // Retrieve category data from the form
        //String categoryName = book.getCategory().getName();
        Optional<Category> category = categoryService.findByCategoryNameIgnoreCase(book.getCategory().getName());
        if (category.isPresent()) {
            book.setCategory(category.get());
        } else {
            categoryService.saveNewCategory(book.getCategory());
        }
    }

    private void retrieveAuthorData(Book book) {
        // Retrieve author data from the form
        String authorFirstName = book.getAuthor().getFirstName();
        String authorLastName = book.getAuthor().getLastName();

        // Check if the author already exists in the database
        Optional<Author> author = authorService.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(authorFirstName, authorLastName);
        if (author.isPresent()) {
            // If the author exists, associate the book with the existing author
            book.setAuthor(author.get());
        } else {
            // If the author doesn't exist, save the author to the database, new author no risk of duplicate
            // this needs to be done before saving a book as it is part of a book
            authorService.saveNewAuthor(book.getAuthor());
        }
    }

    @Override
    public Book saveUpdatedBook(Book book) {
            return bookRepository.save(book);
    }

    @Override
    public Book checkWhichBookToBeSaved(Book book) throws NotFoundException {
        // Retrieve id and author data from the form
        Long bookId = book.getId();
        String authorFirstName = book.getAuthor().getFirstName();
        String authorLastName = book.getAuthor().getLastName();
        // Check if the author already exists in the database
        Optional<Book> existingId = findBookById(bookId);
        Optional<Author> existingAuthor = authorService.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(authorFirstName, authorLastName);

        // my update problem resides here
        if (existingAuthor.isPresent()) {
            // If the author exists, associate the book with the existing author
            book.setAuthor(existingAuthor.get());
        } else {
            // If the author doesn't exist, save the author to the database, this will be a new author so no risk of a duplication
            authorService.saveNewAuthor(book.getAuthor());
        }

        if (existingId.isPresent()){
            book.setTitle(book.getTitle());
            book.setAuthor(book.getAuthor());
            return saveUpdatedBook(book);
        }

        // Save the book
        if (existingId.isEmpty()){
            return addBook(book);
        }
        // returning a default empty book - this line should never be reached
        return new Book();
    }


    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Optional<Book> updateBookById(Long id, Book updateBook) {
        // Retrieve the existing book from the database by its id
        Optional<Book> existingBookId = bookRepository.findById(id);

        // Check if the book with the specified id exists in the database
        if (existingBookId.isPresent()) {
            // Get the existing book from the Optional
            Book existingBook = existingBookId.get();

            // Update the properties of the existing book
            existingBook.setTitle(updateBook.getTitle());
            existingBook.setCategory(updateBook.getCategory());

            // Update author details if provided
            if (updateBook.getAuthor() != null) {
                existingBook.getAuthor().setFirstName(updateBook.getAuthor().getFirstName());
                existingBook.getAuthor().setLastName(updateBook.getAuthor().getLastName());
                // Update other properties of the Author as needed
            }

            // Save the updated book record to the database
            Book updatedBook = bookRepository.save(existingBook);

            // Return the updated book wrapped in an Optional
            return Optional.of(updatedBook);
        } else {
            // If the book record with the specified id doesn't exist, return an empty Optional
            return Optional.empty();
        }
    }
}
