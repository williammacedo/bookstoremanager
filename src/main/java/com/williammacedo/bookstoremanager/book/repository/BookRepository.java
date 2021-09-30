package com.williammacedo.bookstoremanager.book.repository;

import com.williammacedo.bookstoremanager.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select book " +
            "from Book book " +
            "inner join fetch book.author " +
            "inner join fetch book.publisher")
    List<Book> getBooksAuthorsAndPublishers();

    Optional<Book> findByNameAndIsbn(String name, String isbn);

    @Query("select book " +
            "from Book book " +
            "inner join fetch book.author " +
            "inner join fetch book.publisher " +
            "where book.id = :id")
    Optional<Book> getBooksAuthorsAndPublishers(@Param("id") Long id);

    @Query("select book " +
            "from Book book " +
            "inner join fetch book.users users " +
            "where users.username = :username")
    List<Book> getBooksByUser(@Param("username") String username);

    Optional<Book> findByNameAndIsbnAndIdNot(String name, String isbn, Long id);
}
