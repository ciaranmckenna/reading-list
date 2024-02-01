package com.ciaranmckenna.readinglistapp.dao.repository;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

//    List<Author> findByFirstNameOrLastNameContainingIgnoreCase(String firstName, String lastName);

    @Query("SELECT a FROM Author a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND LOWER(a.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Author> findByFirstNameAndLastNameContainingIgnoreCase(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
