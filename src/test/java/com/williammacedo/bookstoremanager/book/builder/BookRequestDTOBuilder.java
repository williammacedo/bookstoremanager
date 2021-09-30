package com.williammacedo.bookstoremanager.book.builder;

import com.williammacedo.bookstoremanager.book.dto.BookRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class BookRequestDTOBuilder {

    @Builder.Default
    String name = "It, a coisa";
    @Builder.Default
    String isbn = "9788401354038";
    @Builder.Default
    Integer pages = 1138;
    @Builder.Default
    Integer chapters = 64;
    @Builder.Default
    Long authorId = 1L;
    @Builder.Default
    Long publisherId = 1L;

    public BookRequestDTO buildBookRequestDTO() {
        return new BookRequestDTO(name, isbn, pages, chapters, authorId, publisherId);
    }
}
