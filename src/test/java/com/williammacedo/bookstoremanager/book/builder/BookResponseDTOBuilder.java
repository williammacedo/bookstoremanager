package com.williammacedo.bookstoremanager.book.builder;

import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class BookResponseDTOBuilder {

    @Builder.Default
    Long id = 1L;
    @Builder.Default
    String name = "It a coisa";
    @Builder.Default
    String isbn = "9788401354038";
    @Builder.Default
    Long pages = 1138L;
    @Builder.Default
    Long chapters = 64L;
    @Builder.Default
    AuthorDTO author = AuthorDTO.builder().id(1L).build();
    @Builder.Default
    PublisherDTO publisher = PublisherDTO.builder().id(1L).build();

    public BookResponseDTO buildBookResponseDTO() {
        return new BookResponseDTO(id, name, isbn, pages, chapters, author, publisher);
    }
}
