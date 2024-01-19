package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.model.AuthorModel;
import com.ciaranmckenna.readinglistapp.model.BookModel;
import com.ciaranmckenna.readinglistapp.service.ReadingListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/author/")
@Controller
public class AuthorController {

    private final ReadingListService readingListService;

    public AuthorController(ReadingListService readingListService) {
        this.readingListService = readingListService;
    }

    @GetMapping("list")
    public String findAllAuthors(Model model){
        List<AuthorModel> authorModelList = readingListService.findAllAuthors();
        model.addAttribute("author", authorModelList);
        return "authors/list-authors";
    }

    @GetMapping("{id}")
    public String findAuthorById(@PathVariable int id, Model model) throws NotFoundException {
        Author authorById = readingListService.findAuthorById(id);
        if (authorById != null){
            model.addAttribute("author", authorById);
        }
        return "authors/list-authors";
    }

    @GetMapping("registration")
    public String showFormForAdd(Model model){
        Author author = new Author();
        model.addAttribute("author", author);
        return "authors/author-form";
    }

    @PostMapping("add")
    public String addAuthor(String firstName, String lastName){
        readingListService.addAuthor(firstName, lastName);
        return "redirect:/author/list";
    }

}
