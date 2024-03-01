package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryRecord> findByCategoryRecordNameContainingIgnoreCase(String name);

    Optional<Category> findByCategoryNameIgnoreCase(String name);

    List<Category> findAllCategories();

    List<CategoryRecord> findAllCategoryRecords();

    Category saveNewCategory(Category category);

    Category updateCategory(Category category) throws NotFoundException;

    Optional<Category> findById(Long categoryId);

    void deleteById(Long categoryId);
}
