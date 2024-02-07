package com.ciaranmckenna.readinglistapp.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "author_detail")
@Data
@NoArgsConstructor
public class AuthorDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "citizenship")
    private String citizenship;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;

    public AuthorDetail(String citizenship) {
        this.citizenship = citizenship;
    }
}
