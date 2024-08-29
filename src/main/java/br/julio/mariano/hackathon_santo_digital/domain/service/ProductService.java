package br.julio.mariano.hackathon_santo_digital.domain.service;

import org.openapitools.model.ProductPostDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.ProductRepository;
import br.julio.mariano.hackathon_santo_digital.util.mapper.ProductMapper;
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

}
