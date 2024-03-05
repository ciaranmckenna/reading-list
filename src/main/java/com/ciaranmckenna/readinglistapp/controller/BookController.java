package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.service.AuthorService;
import com.ciaranmckenna.readinglistapp.service.BookService;
import com.ciaranmckenna.readinglistapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

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
    public String getBookByTitleContaining(@RequestParam(required = false) String title, Model model ) {
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
    public String showFormForAdd(@Valid Model model){

        // Populate the model with the list of authors and categories that is required for adding
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);

        // Add an empty Book object to bind form data
        Book book = new Book();
        model.addAttribute("book", book);

        return "books/book-form";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                logger.debug(objectError.toString());
            });
            return "books/book-form";
        }
        bookService.addBook(book);
        return "redirect:/books/list";
    }

    @GetMapping("/update")
    public String showBookUpdateForm(@RequestParam("id") Long id, Model model) throws NotFoundException {
        Optional<Book> book = bookService.findBookById(id); // returning a book entity object as that's what was entered originally
        model.addAttribute("book", book);
        return "books/book-update";
    }

    @PostMapping("/updateBook")
    public String updateBook(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult ) throws NotFoundException {
        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                logger.debug(objectError.toString());
            });
            return "books/book-update";
        }
        bookService.updateBook(book);
        return "redirect:/books/list";
    }

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
