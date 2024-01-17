package com.ciaranmckenna.readinglistapp.controller.payload;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookResponse {

    private int id;
    private String title;
    private Author author;

}
