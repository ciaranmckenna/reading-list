package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Book;
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

    @Override
    public List<BookRecord> findAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(Mapper::mapBookEntityToBookRecord)
                .sorted(Comparator.comparing(BookRecord::title))
                .toList();
    }

    @Override
    public BookRecord findBookById(int id) throws NotFoundException {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            return Mapper.mapBookEntityToBookRecord(optionalBook.get());
        } else {
            String errorMessage = String.format("No book id: %s found", id);
            log.info(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public List<BookRecord> findByTitleContainingIgnoreCase(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(Mapper::mapBookEntityToBookRecord)
                .collect(Collectors.toList());
    }

    @Override
    public BookRecord getBookDetails(int id) {
        Optional<Book> currentBook = bookRepository.findById(id);
        return currentBook.map(Mapper::mapBookEntityToBookRecord).orElse(null);
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
}
