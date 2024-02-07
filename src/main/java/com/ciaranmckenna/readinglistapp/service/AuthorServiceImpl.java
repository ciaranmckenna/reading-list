package com.ciaranmckenna.readinglistapp.service;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.repository.AuthorRepository;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorRecord> findAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(Mapper::mapAuthorEntityToAuthorRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorRecord> findByAuthorNameContainingIgnoreCase(String firstName, String lastName) {
        List<Author> authors;
        if (firstName != null && !firstName.isEmpty()) {
            // Search by first name
            authors = authorRepository.findByFirstNameContainingIgnoreCase(firstName);
        } else if (lastName != null && !lastName.isEmpty()) {
            // Search by last name
            authors = authorRepository.findByLastNameContainingIgnoreCase(lastName);
        } else {
            // If neither first name nor last name is provided, return all authors
            return Collections.emptyList();
        }
        return mapToAuthorRecordList(authors);
    }

    private List<AuthorRecord> mapToAuthorRecordList(List<Author> authors) {
        return authors.stream()
                .map(Mapper::mapAuthorEntityToAuthorRecord)
                .collect(Collectors.toList());
    }

    @Override
    public Author findAuthorById(int id) throws NotFoundException {
        Optional<Author> optionalAuthorId = authorRepository.findById(id);
        return optionalAuthorId.orElseThrow(() -> new NotFoundException("No author id found"));
    }

    @Override
    public Author addAuthor(Author author) {
        Optional<Author> existingAuthor = findAuthorByFirstNameAndLastName(author.getFirstName(), author.getLastName());

        return existingAuthor.orElseGet(() -> authorRepository.save(author));
    }

    public Optional<Author> findAuthorByFirstNameAndLastName(String firstName, String lastName) {
        return authorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    @Override
    public void deleteAuthorById(int id){
        authorRepository.deleteById(id);
    }
}
