package com.williammacedo.bookstoremanager.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @ISBN
    private String isbn;

    @NotNull
    @Max(3000)
    private Long pages;

    @NotNull
    @Max(100)
    private Long chapters;

    @NotNull
    private Long authorId;

    @NotNull
    private Long publisherId;
}
