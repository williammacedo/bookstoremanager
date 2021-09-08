package com.williammacedo.bookstoremanager.user.repository;

import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import com.williammacedo.bookstoremanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select new com.williammacedo.bookstoremanager.user.dto.UserDTO(" +
            "user.id," +
            "user.name," +
            "user.age," +
            "user.gender," +
            "user.email," +
            "user.username," +
            "user.password," +
            "user.birthDate" +
            ") from User user")
    List<UserDTO> findAllAsDTO();

    Optional<User> findByUsernameOrEmail(String username, String email);
}
