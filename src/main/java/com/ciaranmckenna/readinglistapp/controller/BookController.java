package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.model.BookModel;
import com.ciaranmckenna.readinglistapp.service.ReadingListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/book/")
@Controller
public class BookController {

    private final ReadingListService readingListService;

    public BookController(ReadingListService readingListService) {
        this.readingListService = readingListService;
    }

    @GetMapping("list")
    public String findAllBooks(Model model){
        List<BookModel> bookModelList = readingListService.findAllBooks();
        model.addAttribute("book", bookModelList);
        return "books/list-books";
    }

    @GetMapping("{id}")
    public String getBookDetails(@PathVariable int id, Model model) throws NotFoundException {
        BookModel bookModel = readingListService.getBookDetails(id);
        if (bookModel != null) {
            model.addAttribute("book", bookModel);
        }
        return "books/list-books";
    }

    @GetMapping("search")
    public String showFormForSearch(Model model){
        Book book = new Book();
        model.addAttribute("book", book);
        return "books/book-search";
    }

    @GetMapping("title")
    public String getBookByTitle(@RequestParam String title, Model model){
        List<BookModel> bookByTitle = readingListService.findBookByTitle(title);
        model.addAttribute("book", bookByTitle);
        return "books/list-books";
    }

    @GetMapping("registration")
    public String showFormForAdd(Model model){
        Book book = new Book();
        model.addAttribute("book", book);
        return "books/book-form";
    }

    @PostMapping("add")
    public String addBook(@ModelAttribute("book") Book book){
        readingListService.addBook(book);
        return "redirect:/book/list";
    }
}
