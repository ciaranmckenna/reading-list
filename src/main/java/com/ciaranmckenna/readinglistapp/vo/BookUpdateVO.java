package com.ciaranmckenna.readinglistapp.vo;

import lombok.Data;
@Data
public class BookUpdateVO {

    private int id;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private int categoryId;
    private String categoryName;

}
