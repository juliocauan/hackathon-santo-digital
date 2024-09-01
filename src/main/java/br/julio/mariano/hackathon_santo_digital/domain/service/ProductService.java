package br.julio.mariano.hackathon_santo_digital.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.openapitools.model.ProductDetailsDTO;
import org.openapitools.model.ProductFilter;
import org.openapitools.model.ProductGetDTO;
import org.openapitools.model.ProductPostDTO;
import org.openapitools.model.ProductPutDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.julio.mariano.hackathon_santo_digital.domain.model.Product;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.ProductRepository;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.specification.ProductSpecification;
import br.julio.mariano.hackathon_santo_digital.util.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product register(@Valid ProductPostDTO productPostDTO) {
        Product newProduct = productRepository.save(ProductMapper.INSTANCE.toEntity(productPostDTO));
        log.info("Register Product: " + LocalDateTime.now() + newProduct.toString());
        return newProduct;
    }

    @Transactional(readOnly = true)
    public List<ProductGetDTO> getAll(@Valid ProductFilter filter, Pageable pageable) {
        List<ProductGetDTO> fetchedProducts = productRepository.findAll(ProductSpecification.filter(filter), pageable)
                .map(ProductMapper.INSTANCE::toGetDto).toList();
        log.info("Get All Product: " + LocalDateTime.now() + fetchedProducts.toString());
        return fetchedProducts;
    }

    @Transactional(readOnly = true)
    public ProductDetailsDTO getById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
        ProductDetailsDTO fetchedProduct = ProductMapper.INSTANCE.toDetailsDto(product);
        log.info("Get Product: " + LocalDateTime.now() + fetchedProduct.toString());
        return fetchedProduct;
    }

    public void update(@Valid ProductPutDTO productPutDTO, Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
        ProductMapper.INSTANCE.update(productPutDTO, product);
        productRepository.save(product);
        log.info("Update Product: " + LocalDateTime.now() + product.toString());
    }

    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
        log.info("Delete Product: " + LocalDateTime.now() + product.toString());
        productRepository.deleteById(product.getId());
    }

}
