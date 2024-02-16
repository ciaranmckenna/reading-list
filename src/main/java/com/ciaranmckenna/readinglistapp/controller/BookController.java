package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.repository.AuthorRepository;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import com.ciaranmckenna.readinglistapp.service.AuthorService;
import com.ciaranmckenna.readinglistapp.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    private static final Logger logger = LogManager.getLogger(BookController.class);


    @GetMapping("/list")
    public String findAllBooks(Model model) {
        List<BookRecord> bookRecordList = bookService.findAllBooks();
        model.addAttribute("book", bookRecordList);
        return "books/list-books";
    }

    @GetMapping("/{id}")
    public String getBookDetails(@PathVariable Long id, Model model) throws NotFoundException {
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
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        // Add an empty Book object to bind form data
        Book book = new Book();
        model.addAttribute("book", book);

        return "books/book-form";
    }

    @PostMapping("/add") // two endpoints that are the same --- question this?
    public String addBook(@ModelAttribute("book") Book book){
        // Retrieve author data from the form
        String authorFirstName = book.getAuthor().getFirstName();
        String authorLastName = book.getAuthor().getLastName();
        // this logic should be performed in the service refactor when fixed
        // Check if the author already exists in the database
        Optional<Author> existingAuthor = authorService.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(authorFirstName, authorLastName);
        // my update problem resides here
        if (existingAuthor.isPresent()) {
            // If the author exists, associate the book with the existing author
            book.setAuthor(existingAuthor.get());
        } else {
            // If the author doesn't exist, save the author to the database, new author no risk of duplicate
            authorService.saveNewAuthor(book.getAuthor());
        }

        // Save the book
        bookService.addBook(book);
        return "redirect:/books/list";
    }

    @GetMapping("/update") // should i change bookId to id
    public String showBookUpdateForm(@RequestParam("id") Long id, Model model) throws NotFoundException {
        //BookRecord bookById = bookService.findBookById(id);
        if (id != null) {
            logger.debug("DEBUG ERROR CHECK CIARAN --------------{id} : ", id);
        } else {
            logger.debug("DEBUG ERROR CHECK CIARAN -------------- Book ID is null");
        }
        Optional<Book> book = bookService.findBookById(id); // returning a book entity object as that's what was entered originally
        model.addAttribute("book", book);
        return "books/book-update";
    }

    @PostMapping("/updateBook") // two endpoints that are the same --- question this?
    public String updateBook(@ModelAttribute("book") Book book) throws NotFoundException {
        // Retrieve id and author data from the form
        Long bookId = book.getId();
        String authorFirstName = book.getAuthor().getFirstName();
        String authorLastName = book.getAuthor().getLastName();
        // this logic should be performed in the service refactor when fixed
        // Check if the author already exists in the database
        Optional<Book> existingId = bookService.findBookById(bookId);
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
            bookService.saveUpdatedBook(book);
        }

        // Save the book
        if (existingId.isEmpty()){
            bookService.addBook(book); // what is the value of the argument book at this point
        }
        return "redirect:/books/list";
    }

//    @PostMapping("/update/{id}")
//    public String updateBook(@PathVariable("id") Long id, @ModelAttribute("book") Book book){
//        bookService.updateBookById(id, book);
//        return "redirect:/books/list";
//    }

    @GetMapping("/delete/{bookId}")
    public String deleteBook(@PathVariable("bookId") Long id){
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
