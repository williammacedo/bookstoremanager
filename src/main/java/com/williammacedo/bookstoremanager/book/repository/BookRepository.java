package com.williammacedo.bookstoremanager.book.repository;

import com.williammacedo.bookstoremanager.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}
