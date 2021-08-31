package com.williammacedo.bookstoremanager.publisher.repository;

import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
import com.williammacedo.bookstoremanager.publisher.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @Query("select new com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO(" +
            "pub.id, " +
            "pub.name, " +
            "pub.code, " +
            "pub.foundationDate" +
            ") " +
            "from Publisher pub")
    List<PublisherDTO> findAllAsDTO();

    Optional<Publisher> findByNameOrCode(String name, String code);
}
