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
    Long id =10L;
    @Builder.Default
    String name = "Joao Victor";
    @Builder.Default
    int age = 17;

    public AuthorDTO buildAuthorDTO() {
        return new AuthorDTO(id,name,age);
    }
}
