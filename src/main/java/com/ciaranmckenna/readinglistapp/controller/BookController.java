package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dao.repository.BookRepository;
import com.ciaranmckenna.readinglistapp.dao.repository.CategoryRepository;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.dto.BookRecord;
import com.ciaranmckenna.readinglistapp.service.ReadingListService;
import com.ciaranmckenna.readinglistapp.vo.BookUpdateVO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RequestMapping("/book/")
@Controller
@AllArgsConstructor
@Slf4j
public class BookController {

    private final ReadingListService readingListService;

    //Repositories should be handled within @Services - controllers should only expose, validate, etc...
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;


    @GetMapping("list")
    public String findAllBooks(Model model){
        List<BookRecord> bookRecordList = readingListService.findAllBooks();
        model.addAttribute("book", bookRecordList);
        return "books/list-books";
    }

    @GetMapping("{id}")
    public String getBookDetails(@PathVariable int id, Model model) throws NotFoundException {
        BookRecord bookRecord = readingListService.getBookDetails(id);
        if (bookRecord != null) {
            model.addAttribute("book", bookRecord);
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
    public String getBookByTitleLike(@RequestParam  String title, Model model ){
        // allow parameterless GET request for /title to return all records
        if (title.isEmpty()) {
            title = ""; // empty String signifies broadest possible search
        }

        List<BookRecord> bookListByTitle = readingListService.findByTitleContainingIgnoreCase(title);

        if (bookListByTitle.isEmpty()){
            return "books/error-list-books";
        } else {
            model.addAttribute("book", bookListByTitle);
            return  "books/list-books";
        }
    }

    @GetMapping("registration")
    public String showFormForAdd(Model model){
        Book book = new Book();
        List<CategoryRecord> categories = readingListService.findAllCategories();
        //List<Category> categories = readingListService.findAllCategories();
        //Category categories = new Category();
        model.addAttribute("book", book);
        model.addAttribute("categories", categories);
        return "books/book-form";
    }

    @PostMapping("add")
    public String addBook(@ModelAttribute("book") Book book, @RequestParam("categoryName") String categoryName) {
        if (book.getCategory() != null && book.getCategory().getId() != null && book.getCategory().getId().equals("NEW_CATEGORY")) {
            // User selected option to create a new category
            Category newCategory = new Category(categoryName);
            Category savedCategory = readingListService.addCategory(newCategory); 
            book.setCategory(savedCategory);
        }

        // Handle saving the book to the database
        readingListService.addBook(book);

        return "redirect:/book/list";
    }

//    @PostMapping("add")
//    public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult result){
//        if (result.hasErrors()){
//            return "error-list-books";
//        }
//
//        Category selectedCategory = book.getCategory();
//        if (selectedCategory !=null && selectedCategory.getId() == null){
//            book.setCategory(selectedCategory);
//        }
//        readingListService.addBook(book);
//        return "redirect:/book/list";
//    }

//    @PostMapping("add")
//    public String addBook(@ModelAttribute("book") Book book, @ModelAttribute("category") CategoryRecord categoryRecord) {
//        Category selectedCategory = convertToCategory(categoryRecord);
//
//        if (selectedCategory.getId() == null) {
//            // Set other properties if needed
//            // Ensure that the selectedCategory is managed by Hibernate
//            selectedCategory = categoryRepository.save(selectedCategory);
//        }
//
//        book.setCategory(selectedCategory);
//        readingListService.addBook(book);
//
//        return "redirect:/book/list";
//    }

    @GetMapping("update/{bookId}")
    public String updateBook(@PathVariable("bookId") int id, Model model) throws NotFoundException {
        Book bookById = readingListService.findBookById(id);
        model.addAttribute("book", bookById);
        return "books/book-form";
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateBook(@Valid @RequestBody BookUpdateVO bookUpdateVO) {

        log.info("Don't forget to add logs to your app ;)");

        //implement this in a service
        try {

            Book bookToUpdate = readingListService.findBookById(bookUpdateVO.getId());

            bookToUpdate.setTitle(bookUpdateVO.getTitle());
            //...

            if (bookUpdateVO.getCategoryId() == 0) {
                Category newCat = new Category(bookUpdateVO.getCategoryName());
                newCat = categoryRepository.save(newCat);
                bookToUpdate.setCategory(newCat);
            } else {
                Optional<Category> optCat = categoryRepository.findById(bookUpdateVO.getCategoryId());
                if (optCat.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category id");
                } else {
                    bookToUpdate.setCategory(optCat.get());
                }
            }

            Book savedBook = bookRepository.save(bookToUpdate);

            return ResponseEntity.ok("Book updated successfully");
            //OR: return ResponseEntity.ok(savedBook);
            //OR: redirect to other thymeleaf template
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update book: " + e.getMessage());
        }
    }
    @GetMapping("delete/{bookId}")
    public String deleteBook(@PathVariable("bookId") int id){
        readingListService.deleteBookById(id);
        return "redirect:/book/list";
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        modelAndView.setViewName("error-page");
        return modelAndView;
    }

    public Category convertToCategory(CategoryRecord categoryRecord) {
        Category category = new Category();
        category.setId(categoryRecord.id());
        category.setName(categoryRecord.name());
        // Set any other properties if needed
        return category;
    }
}
