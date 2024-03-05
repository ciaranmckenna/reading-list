package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Category;
import com.ciaranmckenna.readinglistapp.dao.repository.CategoryRepository;
import com.ciaranmckenna.readinglistapp.dto.CategoryRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryRecord> findByCategoryRecordNameContainingIgnoreCase(String name) { //  this should be a list
        List<Category> categories;
        if (name !=null && !name.isEmpty()){
            categories = categoryRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name);
        } else return Collections.emptyList();
    return Mapper.categoryListMappedToCategoryRecordList(categories);
    }

    @Override
    public Optional<Category> findByCategoryNameIgnoreCase(String name){
        return categoryRepository.findByNameIgnoreCase(name);
    }

    // used to retrieve entities
    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    /*
    *  @Override
    public List<BookRecord> findAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(Mapper::mapBookEntityToBookRecord)
                .sorted(Comparator.comparing(BookRecord::title))
                .toList();
    }
    *
    * */

    @Override
    public List<CategoryRecord> findAllCategoryRecords() {
        List<Category> categories = findAllCategories();
        categories.stream()
                .map(Mapper::mapCategoryEntityToCategoryRecord)
                .sorted(Comparator.comparing(CategoryRecord::name))
                .toList();
        return Mapper.categoryListMappedToCategoryRecordList(categories);
    }

    @Override
    public Category saveNewCategory(Category category) {
        Optional<Category> existingCategory = findByCategoryNameIgnoreCase(category.getName());
        if (existingCategory.isPresent()){
            return existingCategory.get();
        }else {
            return categoryRepository.save(category);
        }
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

    @Override
    public void deleteById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
