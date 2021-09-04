package com.williammacedo.bookstoremanager.user.dto;

import com.williammacedo.bookstoremanager.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private int age;
    private Gender gender;
    private String email;
    private String username;
    private String password;
    private LocalDate birthDate;
}
