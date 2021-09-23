package com.williammacedo.bookstoremanager.user.repository;

import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import com.williammacedo.bookstoremanager.user.dto.UserDetailsDTO;
import com.williammacedo.bookstoremanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

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
            "user.birthDate," +
            "user.role" +
            ") from User user")
    List<UserDTO> findAllAsDTO();

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query(
        "select user " +
        "from User user " +
        "where user.id <> :id and " +
        "(user.username = :username or user.email = :email)"
    )
    Optional<User> findByUsernameOrEmailAndIdNot(@Param("id") Long id, @Param("username") String username, @Param("email") String email);

    @Query(
        "select new  com.williammacedo.bookstoremanager.user.dto.UserDetailsDTO(" +
            "user.username," +
            "user.password," +
            "user.role" +
        ")" +
        "from User user " +
        "where user.username = :username"
    )
    Optional<UserDetailsDTO> findByUsername(String username);
}
