package com.ciaranmckenna.readinglistapp.dto;

import java.util.List;

public record CategoryRecord(Long id, String name, List<String> titles, List<String> authorFirstNames, List<String> authorLastNames) {
}

