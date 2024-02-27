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
    public List<Author> findAll() {
        return authorRepository.findAll();
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
        return Mapper.mapToAuthorRecordList(authors);
    }

    @Override
    public Optional<Author> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName) {
        return authorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    @Override
    public Author findAuthorById(Long id) throws NotFoundException {
        Optional<Author> optionalAuthorId = authorRepository.findById(id);
        return optionalAuthorId.orElseThrow(() -> new NotFoundException("No author id found"));
    }

    @Override
    public Author addAuthor(Author author) {
        Optional<Author> existingAuthor = findAuthorByFirstNameAndLastName(author.getFirstName(), author.getLastName()); // replace with findByFirstNameIgnoreCaseAndLastNameIgnoreCase

        return existingAuthor.orElseGet(() -> authorRepository.save(author));
    }

    @Override
    public Author saveNewAuthor(Author author) {
       return authorRepository.save(author);
    }

    public Optional<Author> findAuthorByFirstNameAndLastName(String firstName, String lastName) { /// change ... this method will be redundant
        return authorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    @Override
    public void deleteAuthorById(Long id){
        authorRepository.deleteById(id);
    }
}
