package com.williammacedo.bookstoremanager.book.entity;

import com.williammacedo.bookstoremanager.author.entity.Author;
import com.williammacedo.bookstoremanager.entity.Auditable;
import com.williammacedo.bookstoremanager.publisher.entity.Publisher;
import com.williammacedo.bookstoremanager.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Book extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String isbn;

    @Column(columnDefinition = "integer default 0")
    private int pages;

    @Column(columnDefinition = "integer default 0")
    private int chapters;

    @ManyToOne
    private Author author;

    @ManyToOne
    private Publisher publisher;

    @ManyToMany(mappedBy = "books")
    private List<User> users;
}
