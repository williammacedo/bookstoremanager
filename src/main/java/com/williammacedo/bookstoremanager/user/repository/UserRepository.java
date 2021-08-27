package com.williammacedo.bookstoremanager.user.repository;

import com.williammacedo.bookstoremanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
