package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.repository.AuthorRepository;
import com.ciaranmckenna.readinglistapp.dao.repository.BookRepository;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
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
    private final AuthorRepository authorRepository;

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
        Optional<Book> listTitleContaining = bookRepository.findByTitleContainingIgnoreCase(book.getTitle());

        if (listTitleContaining.isPresent()){
            return listTitleContaining.get();
        }else {
            return bookRepository.save(book);
        }
    }

    @Override
    public Book saveUpdatedBook(Book book) {
            return bookRepository.save(book);
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
