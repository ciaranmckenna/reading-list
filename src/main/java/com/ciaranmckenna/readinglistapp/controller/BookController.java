package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.repository.AuthorRepository;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import com.ciaranmckenna.readinglistapp.service.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookServiceImpl bookService;
    private final AuthorRepository authorRepository;

    @GetMapping("/list")
    public String findAllBooks(Model model) {
        List<BookRecord> bookRecordList = bookService.findAllBooks();
        model.addAttribute("book", bookRecordList);
        return "books/list-books";
    }

    @GetMapping("/{id}")
    public String getBookDetails(@PathVariable int id, Model model) throws NotFoundException {
        BookRecord bookRecord = bookService.getBookDetails(id);
        model.addAttribute("book", bookRecord);
        return "books/list-books";
    }

    @GetMapping("/search")
    public String showFormForSearch(Model model){
        Book book = new Book();
        model.addAttribute("book", book);
        return "books/book-search";
    }

    @GetMapping("/title")
    public String getBookByTitleLike(@RequestParam(required = false) String title, Model model ) {
        // allow parameterless GET request for /title to return all records
        if (title.isEmpty()) {
            return "books/list-books";
        }

        List<BookRecord> bookListByTitle = bookService.findByTitleContainingIgnoreCase(title);

        if (bookListByTitle.isEmpty()){
            return "books/error-list-books";
        } else {
            model.addAttribute("book", bookListByTitle);
            return  "books/list-books";
        }
    }

    @GetMapping("/registration")
    public String showFormForAdd(Model model){
        // Populate the model with the list of authors
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);

        // Add an empty Book object to bind form data
        Book book = new Book();
        model.addAttribute("book", book);

        return "books/book-form";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        // Populate the model with the list of authors
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);

        // Add an empty Book object to bind form data
        model.addAttribute("book", new Book());

        return "books/book-form";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book){
        // Retrieve author data from the form
        String authorFirstName = book.getAuthor().getFirstName();
        String authorLastName = book.getAuthor().getLastName();

        // Check if the author already exists in the database
        Optional<Author> existingAuthor = authorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(authorFirstName, authorLastName);
        if (existingAuthor.isPresent()) {
            // If the author exists, associate the book with the existing author
            book.setAuthor(existingAuthor.get());
        } else {
            // If the author doesn't exist, save the author to the database
            authorRepository.save(book.getAuthor());
        }

        // Save the book
        bookService.addBook(book);
        return "redirect:/books/list";
    }

    @GetMapping("/update/{bookId}")
    public String updateBook(@PathVariable("bookId") int id, Model model) throws NotFoundException {
        BookRecord bookById = bookService.findBookById(id);
        model.addAttribute("book", bookById);
        return "books/book-form";
    }

    @GetMapping("/delete/{bookId}")
    public String deleteBook(@PathVariable("bookId") int id){
        bookService.deleteBookById(id);
        return "redirect:/books/list";
    }

    @ExceptionHandler()
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        modelAndView.setViewName("error-page");
        return modelAndView;
    }
}
