package com.williammacedo.bookstoremanager.user.builder;

import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import com.williammacedo.bookstoremanager.user.enums.Gender;
import com.williammacedo.bookstoremanager.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class UserDTOBuilder {
    
    @Builder.Default
    Long id = 100L;
    @Builder.Default
    String name = "William";
    @Builder.Default
    int age = 33;
    @Builder.Default
    Gender gender = Gender.MALE;
    @Builder.Default
    String email = "william@email.com";
    @Builder.Default
    String username = "william_21";
    @Builder.Default
    String password = "123456";
    @Builder.Default
    LocalDate birthDate = LocalDate.of(1990, 1, 1);
    @Builder.Default
    Role role = Role.USER;

    public UserDTO buildUserDTO() {
        return new UserDTO(id,name,age,gender,email,username,password,birthDate, role);
    }
}
