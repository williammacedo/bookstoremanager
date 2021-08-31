package com.williammacedo.bookstoremanager.publisher.builder;

import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class PublisherDTOBuilder {

    @Builder.Default
    Long id =100L;
    @Builder.Default
    String name = "Arqueiro";
    @Builder.Default
    String code = "123456789";
    @Builder.Default
    LocalDate foundationDate = LocalDate.now();

    public PublisherDTO buildPublisherDTO() {
        return new PublisherDTO(id,name,code,foundationDate);
    }
}

