package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dao.repository.CategoryRepository;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
/*

    @Override
    public List<CategoryRecord> findByCategoryRecordNameIgnoreCase(String name) {
        Optional<Category> categories;
        if (name !=null && !name.isEmpty()){
            categories = categoryRepository.findByNameIgnoreCase(name);
        } else return Collections.emptyList();
    return Mapper.mapToCategoryRecordList(categories);
    }
*/

    @Override
    public Optional<Category> findByCategoryNameIgnoreCase(String name){
        return categoryRepository.findByNameIgnoreCase(name);
    }

    // used to retrieve entities
    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryRecord> findAllCategoryRecords() {
        List<Category> categories = findAllCategories();
        return Mapper.mapToCategoryRecordList(categories);
    }

    @Override
    public Category saveNewCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) throws NotFoundException {
        Optional<Category> existingId = categoryRepository.findById(category.getId());

        if (existingId.isPresent()){
            category.setName(category.getName());
            return saveNewCategory(category);
        }else{
        // returning a default empty category - this line should never be reached
            return new Category();
        }
    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

}
