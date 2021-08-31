package com.williammacedo.bookstoremanager.publisher.mapper;

import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
import com.williammacedo.bookstoremanager.publisher.entity.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PublisherMapper {

    PublisherMapper INSTANCE = Mappers.getMapper(PublisherMapper.class);

    PublisherDTO toDTO(Publisher entity);
    Publisher toModel(PublisherDTO dto);
}
