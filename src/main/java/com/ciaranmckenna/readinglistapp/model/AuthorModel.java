package com.ciaranmckenna.readinglistapp.model;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.AuthorDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorModel {

    private int id;
    private String firstName;
    private String lastName;

    // missing author detail field because it keeps returning null
    public AuthorModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
