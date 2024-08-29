package br.julio.mariano.hackathon_santo_digital.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.openapitools.model.ProductPostDTO;

import br.julio.mariano.hackathon_santo_digital.domain.model.Product;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductPostDTO postDTO);
}
