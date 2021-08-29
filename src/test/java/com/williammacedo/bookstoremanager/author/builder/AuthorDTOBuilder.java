package com.williammacedo.bookstoremanager.author.builder;

import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AuthorDTOBuilder {

    @Builder.Default
    private Long id =1L;
    @Builder.Default
    private String name = "Rodrigo Peleias";
    @Builder.Default
    private int age = 32;

    public AuthorDTO buildAuthorDTO() {
        return new AuthorDTO(id,name,age);
    }
}
