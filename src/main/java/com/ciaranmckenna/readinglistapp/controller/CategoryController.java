package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/registration")
    public String showCategoryForm(Category category, Model model){
        // may new Category object here
        model.addAttribute("category", category);
        return "categories/category-form";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") Category category){
        categoryService.saveNewCategory(category);
        return "redirect:/categories/list";
    }

    @GetMapping("/list")
    public String findAllCategories(Model model){
        List<CategoryRecord> categories = categoryService.findAllCategoryRecords();
        model.addAttribute("category", categories);
        return "categories/list-categories";
    }

    /*
    @GetMapping("/name")
    public String findAuthorByNameContaining(@RequestParam String firstName, @RequestParam String lastName, Model model){

        if (firstName.isEmpty() && lastName.isEmpty()) {
            firstName = "";
            lastName = "";

        }
        List<AuthorRecord> authorNameContaining = authorService.findByAuthorNameContainingIgnoreCase(firstName, lastName);
        model.addAttribute("author", authorNameContaining);
        return "authors/list-authors";
    }
    *
    }
    * */
    @GetMapping("/search")
    public String showFormForSearch(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "categories/category-search";
    }

    @GetMapping("/name")
    public String findByCategoryName(@RequestParam String name, Model model){
        if (name.isEmpty()){
            name = "";
        }
        List<CategoryRecord> categoryNameContaining = categoryService.findByCategoryRecordNameContainingIgnoreCase(name);
        model.addAttribute("category", categoryNameContaining);
        return "categories/list-categories";
    }

    @GetMapping("/update")
    public String showUpdateCategoryForm(@RequestParam("categoryId") Long categoryId, Model model ){
        Optional<Category> existingCategoryId = categoryService.findById(categoryId);
        model.addAttribute("category", existingCategoryId);
        return "categories/category-update";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(Category category) throws NotFoundException {
        categoryService.updateCategory(category);
        return "redirect:/categories/list";
    }

    @GetMapping("/delete/{categoryId}")
    public String deleteCategoryById(@PathVariable("categoryId") Long categoryId){
        categoryService.deleteById(categoryId);
        return "redirect:/categories/list";
    }

}
