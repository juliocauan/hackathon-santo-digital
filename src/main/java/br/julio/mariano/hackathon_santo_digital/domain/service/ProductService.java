package br.julio.mariano.hackathon_santo_digital.domain.service;

import java.util.List;

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

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void register(@Valid ProductPostDTO productPostDTO) {
        productRepository.save(ProductMapper.INSTANCE.toEntity(productPostDTO));
    }

    @Transactional(readOnly = true)
    public List<ProductGetDTO> getAll(@Valid ProductFilter filter, Pageable pageable) {
        return productRepository.findAll(ProductSpecification.filter(filter), pageable)
                .map(ProductMapper.INSTANCE::toGetDto).toList();
    }

    @Transactional(readOnly = true)
    public ProductGetDTO getById(Integer id) {
        Product fetchedProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
        return ProductMapper.INSTANCE.toGetDto(fetchedProduct);
    }

    public void update(@Valid ProductPutDTO productPutDTO, Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
        ProductMapper.INSTANCE.update(productPutDTO, product);
        productRepository.save(product);
    }

    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
        productRepository.deleteById(product.getId());
    }

}
