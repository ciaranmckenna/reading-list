package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.model.AuthorModel;
import com.ciaranmckenna.readinglistapp.model.BookModel;
import com.ciaranmckenna.readinglistapp.service.ReadingListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        model.addAttribute("authors", authorModelList);

        return "authors/list-authors";
    }

    @GetMapping("showFormForAdd")
    public String showFormForAdd(Model model){

        Author author = new Author();

        model.addAttribute("author", author);

        return "authors/author-form";
    }

    @PostMapping("addAuthor")
    public String addAuthor(String firstName, String lastName, String citizenship){
        readingListService.addAuthor(firstName, lastName, citizenship);
        return "redirect:/author/list";
    }


}
