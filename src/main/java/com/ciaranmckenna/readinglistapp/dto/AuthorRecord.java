package com.ciaranmckenna.readinglistapp.dto;

import java.util.List;

public record AuthorRecord(Long id, String firstName, String lastName, List<String> books) {}
