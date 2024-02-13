package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.service.AuthorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorServiceImpl authorService;

    @GetMapping("/list")
    public String findByAuthorNameContainingIgnoreCase(@RequestParam(name = "firstName", required = false) String firstName,
                                                       @RequestParam(name = "lastName", required = false) String lastName,
                                                       Model model) {
        List<AuthorRecord> authorRecordList;
        if (firstName != null && !firstName.isEmpty() || lastName != null && !lastName.isEmpty()) {
            authorRecordList = authorService.findByAuthorNameContainingIgnoreCase(firstName, lastName);
        } else {
            authorRecordList = authorService.findAllAuthors();
        }
        model.addAttribute("author", authorRecordList);
        return "authors/list-authors";
    }

    @GetMapping("/{id}")
    public String findAuthorById(@PathVariable Long id, Model model) throws NotFoundException {
        Author authorById = authorService.findAuthorById(id);
        if (authorById != null){
            model.addAttribute("author", authorById);
        }
        return "authors/list-authors";
    }

    @GetMapping("/search")
    public String showFormForSearch(Model model){
        Author author = new Author();
        model.addAttribute("author", author);
        return "authors/author-search";
    }

    @GetMapping("/name")
    public String findAuthorByNameContaining(@RequestParam String firstName, @RequestParam String lastName, Model model){

        if (firstName.isEmpty() && lastName.isEmpty()) {
            firstName = ""; // empty String signifies broadest possible search
            lastName = ""; // empty String signifies broadest possible search

        }
        List<AuthorRecord> authorNameContaining = authorService.findByAuthorNameContainingIgnoreCase(firstName, lastName);
        model.addAttribute("author", authorNameContaining);
        return "authors/list-authors";
    }

    @GetMapping("/registration")
    public String showFormForAdd(Model model){
        Author author = new Author();
        model.addAttribute("author", author);
        return "authors/author-form";
    }

    @PostMapping("/add")
    public String addAuthor(@ModelAttribute("author") Author author){
        authorService.addAuthor(author);
        return "redirect:/authors/list";
    }

    @GetMapping("/update/{authorId}")
    public String updateAuthor(@PathVariable("authorId") Long id, Model model) throws NotFoundException {
        Author authorById = authorService.findAuthorById(id);
        model.addAttribute("author", authorById);
        return "authors/author-form";
    }

    @GetMapping("/delete/{authorId}")
    public String deleteAuthor(@PathVariable("authorId") Long id){
        authorService.deleteAuthorById(id);
        return "redirect:/authors/list";
    }
}
