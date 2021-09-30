package com.williammacedo.bookstoremanager.user.entity;

import com.williammacedo.bookstoremanager.book.entity.Book;
import com.williammacedo.bookstoremanager.entity.Auditable;
import com.williammacedo.bookstoremanager.user.enums.Gender;
import com.williammacedo.bookstoremanager.user.enums.Role;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDate birthDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "books_user",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id")
    )
    private List<Book> books;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
}
