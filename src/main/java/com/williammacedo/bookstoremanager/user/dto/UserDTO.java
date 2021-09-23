package com.williammacedo.bookstoremanager.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.williammacedo.bookstoremanager.user.enums.Gender;
import com.williammacedo.bookstoremanager.user.enums.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank
    @Size(max = 255)
    private String name;
    @NotNull
    @Max(120)
    private int age;
    @NotNull
    private Gender gender;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    @ApiModelProperty(example = "01/01/1980")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    @NotNull
    private Role role;
}
