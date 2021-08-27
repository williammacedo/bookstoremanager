package com.williammacedo.bookstoremanager.author.repository;

import com.williammacedo.bookstoremanager.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {}
