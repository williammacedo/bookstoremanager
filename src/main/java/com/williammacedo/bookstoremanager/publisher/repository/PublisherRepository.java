package com.williammacedo.bookstoremanager.publisher.repository;

import com.williammacedo.bookstoremanager.publisher.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {}
