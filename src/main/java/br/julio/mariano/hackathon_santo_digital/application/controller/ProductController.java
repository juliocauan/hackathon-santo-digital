package br.julio.mariano.hackathon_santo_digital.application.controller;

import java.util.List;

import org.openapitools.api.ProductApi;
import org.openapitools.model.OkResponse;
import org.openapitools.model.ProductDetailsDTO;
import org.openapitools.model.ProductFilter;
import org.openapitools.model.ProductGetDTO;
import org.openapitools.model.ProductPostDTO;
import org.openapitools.model.ProductPutDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.julio.mariano.hackathon_santo_digital.domain.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<OkResponse> _registerProduct(@Valid ProductPostDTO productPostDTO) {
        productService.register(productPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OkResponse("Produto registrado com sucesso!"));
    }

    @Override
    public ResponseEntity<List<ProductGetDTO>> _getAllProducts(@Valid ProductFilter filter, Pageable pageable) {
            List<ProductGetDTO> response = productService.getAll(filter, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ProductDetailsDTO> _getProduct(Integer id) {
        ProductDetailsDTO response = productService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<OkResponse> _updateProduct(@Valid ProductPutDTO productPutDTO, Integer id) {
        productService.update(productPutDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(new OkResponse("Produto atualizado com sucesso!"));
    }
    
    @Override
    public ResponseEntity<OkResponse> _deleteProduct(Integer id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new OkResponse("Produto removido com sucesso!"));
    }
    
}
