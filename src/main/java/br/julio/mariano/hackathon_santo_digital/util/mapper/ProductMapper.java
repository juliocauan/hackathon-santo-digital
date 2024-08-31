package br.julio.mariano.hackathon_santo_digital.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.openapitools.model.ProductDetailsDTO;
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

    //TODO check this
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "productNumber", ignore = true)
    @Mapping(target = "standardCost", conditionExpression = "java(putDTO.getStandardCost() != null)", defaultExpression = "java(product.getStandardCost())")
    void update(ProductPutDTO putDTO, @MappingTarget Product product);

    ProductDetailsDTO toDetailsDto(Product fetchedProduct);
}
