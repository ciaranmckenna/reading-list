package com.ciaranmckenna.readinglistapp.dao.entity;

public enum CategoryType {

    FICTION("fiction"),
    NONFICTION("nonfiction"),
    TECHNOLOGY("technology"),
    FINANCE("finance"),
    SPORTS("sports"),
    PSYCHOLOGY("psychology"),
    ;

    private final String CategoryType;

    CategoryType(String categoryType) {
        CategoryType = categoryType;
    }

    public String getCategoryType() {
        return CategoryType;
    }
}
