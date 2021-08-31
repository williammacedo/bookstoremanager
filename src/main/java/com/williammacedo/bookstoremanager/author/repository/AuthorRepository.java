package com.williammacedo.bookstoremanager.author.repository;

import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.author.entity.Author;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameIgnoreCase(String toUpperCase);

    @Query("select new com.williammacedo.bookstoremanager.author.dto.AuthorDTO(" +
            "author.id," +
            "author.name," +
            "author.age)" +
            " from Author author")
    List<AuthorDTO> findAllAsDTO(Sort name);
}
