package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    //List<CategoryRecord> findByCategoryRecordNameIgnoreCase(String name); // am i using this?

    Optional<Category> findByCategoryNameIgnoreCase(String name);

    List<Category> findAllCategories();

    List<CategoryRecord> findAllCategoryRecords();

    Category saveNewCategory(Category category);
}
