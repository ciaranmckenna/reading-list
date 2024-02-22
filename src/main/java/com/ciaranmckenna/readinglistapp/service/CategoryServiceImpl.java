package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dao.repository.CategoryRepository;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;
import com.ciaranmckenna.readinglistapp.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryRecord> findByCategoryRecordNameIgnoreCase(String name) {
        Optional<Category> categories;
        if (name !=null && !name.isEmpty()){
            categories = categoryRepository.findByNameIgnoreCase(name);
        } else return Collections.emptyList();
    return mapToCategoryRecordList(categories);
    }

    @Override
    public Optional<Category> findByCategoryNameIgnoreCase(String name){
        return categoryRepository.findByNameIgnoreCase(name);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }


    @Override
    public Category saveNewCategory(Category category) {
        return categoryRepository.save(category);
    }

    // this should be refactored to a mapper class i think
    private List<CategoryRecord> mapToCategoryRecordList(Optional<Category> categories){
        return categories.stream()
                .map(Mapper::mapCategoryEntityToCategoryRecord)
                .collect(Collectors.toList());
    }

}
