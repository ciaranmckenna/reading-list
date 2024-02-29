package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public String findAllCategories(Model model){
        List<CategoryRecord> categories = categoryService.findAllCategoryRecords();
        model.addAttribute("category", categories);
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

}
