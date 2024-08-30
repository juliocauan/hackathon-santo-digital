package br.julio.mariano.hackathon_santo_digital.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.openapitools.model.ProductGetDTO;
import org.openapitools.model.ProductPostDTO;
import org.openapitools.model.ProductPutDTO;

import br.julio.mariano.hackathon_santo_digital.domain.model.Product;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductPostDTO postDTO);
    
    ProductGetDTO toGetDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", conditionExpression = "java(putDTO.getName() != null)", defaultExpression = "java(product.getName())")
    @Mapping(target = "price", conditionExpression = "java(putDTO.getPrice() != null)", defaultExpression = "java(product.getPrice())")
    @Mapping(target = "number", conditionExpression = "java(putDTO.getNumber() != null)", defaultExpression = "java(product.getNumber())")
    void update(ProductPutDTO putDTO, @MappingTarget Product product);
}
