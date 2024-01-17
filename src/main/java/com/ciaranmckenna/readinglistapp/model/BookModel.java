package com.ciaranmckenna.readinglistapp.model;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class BookModel {

    private int id;
    private String title;
    private String authorFirstName;
    private String authorLastName;

    public BookModel(String title, String authorFirstName, String authorLastName) {
        this.title = title;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
    }
}
