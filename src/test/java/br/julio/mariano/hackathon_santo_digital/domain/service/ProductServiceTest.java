package br.julio.mariano.hackathon_santo_digital.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ProductColour;
import org.openapitools.model.ProductPostDTO;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.julio.mariano.hackathon_santo_digital.config.TestContext;
import br.julio.mariano.hackathon_santo_digital.domain.model.Product;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.ProductRepository;

class ProductServiceTest extends TestContext {

    private final ProductService productService;
    private final ProductRepository productRepository;

    public ProductServiceTest(ObjectMapper objectMapper, MockMvc mockMvc, ProductRepository productRepository, ProductService productService) {
        super(objectMapper, mockMvc);
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void beforeEach() {
        productRepository.deleteAll();
    }

    @Test
    void register() {
        ProductPostDTO expectedDog = new ProductPostDTO(getRandomString(), 42, new BigDecimal("9.46")).colour(ProductColour.AZUL);
        Product fetchedDog;
        assertDoesNotThrow(() -> productService.register(expectedDog));
        fetchedDog = productRepository.findAll().get(0);
        assertEquals(expectedDog.getColour(), fetchedDog.getColour());
        assertEquals(expectedDog.getName(), fetchedDog.getName());
        assertEquals(expectedDog.getNumber(), fetchedDog.getNumber());
        assertEquals(expectedDog.getPrice(), fetchedDog.getPrice());
    }
    
}
