package br.julio.mariano.hackathon_santo_digital.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ProductColour;
import org.openapitools.model.ProductPostDTO;
import org.openapitools.model.ProductPutDTO;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.julio.mariano.hackathon_santo_digital.config.TestContext;
import br.julio.mariano.hackathon_santo_digital.domain.model.Product;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;

class ProductServiceTest extends TestContext {

    private final ProductService productService;
    private final ProductRepository productRepository;

    public ProductServiceTest(ObjectMapper objectMapper, MockMvc mockMvc, ProductRepository productRepository,
            ProductService productService) {
        super(objectMapper, mockMvc);
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void beforeEach() {
        productRepository.deleteAll();
    }

    private final Product generateRandomProduct() {
        return Product.builder()
                .id(null)
                .colour(ProductColour.AZUL)
                .name(getRandomString())
                .number((int) (Math.random() * 100))
                .price(new BigDecimal(Math.round(Math.random() * 100)))
                .build();
    }

    @Test
    void register() {
        ProductPostDTO expectedDog = new ProductPostDTO(getRandomString(), 42, new BigDecimal("9.46"))
                .colour(ProductColour.AZUL);
        Product fetchedDog;
        assertDoesNotThrow(() -> productService.register(expectedDog));
        fetchedDog = productRepository.findAll().get(0);
        assertEquals(expectedDog.getColour(), fetchedDog.getColour());
        assertEquals(expectedDog.getName(), fetchedDog.getName());
        assertEquals(expectedDog.getNumber(), fetchedDog.getNumber());
        assertEquals(expectedDog.getPrice(), fetchedDog.getPrice());
    }

    @Test
    void update() {
        Product oldProduct = productRepository.saveAndFlush(generateRandomProduct());
        Product newProduct;
        ProductPutDTO putDTO = new ProductPutDTO()
                .colour(ProductColour.VERDE)
                .name(getRandomString())
                .number((int) (Math.random() * 100))
                .price(new BigDecimal(Math.round(Math.random() * 100)));
        assertDoesNotThrow(() -> productService.update(putDTO, oldProduct.getId()));
        newProduct = productRepository.findAll().get(0);
        assertEquals(oldProduct.getId(), newProduct.getId());
        assertEquals(putDTO.getColour(), newProduct.getColour());
        assertEquals(putDTO.getName(), newProduct.getName());
        assertEquals(putDTO.getNumber(), newProduct.getNumber());
        assertEquals(putDTO.getPrice().intValue(), newProduct.getPrice().intValue());
    }

    @Test
    void update_branch_withNullParams() {
        Product oldProduct = productRepository.saveAndFlush(generateRandomProduct());
        Product newProduct;
        ProductPutDTO putDTO = new ProductPutDTO()
                .colour(null)
                .name(getRandomString())
                .number(null)
                .price(new BigDecimal(Math.round(Math.random() * 100)));
        assertDoesNotThrow(() -> productService.update(putDTO, oldProduct.getId()));
        newProduct = productRepository.findAll().get(0);
        assertEquals(oldProduct.getId(), newProduct.getId());
        assertEquals(null, newProduct.getColour());
        assertEquals(putDTO.getName(), newProduct.getName());
        assertEquals(oldProduct.getNumber(), newProduct.getNumber());
        assertEquals(putDTO.getPrice().intValue(), newProduct.getPrice().intValue());
    }

    @Test
    void update_error_notFound() {
        ProductPutDTO putDTO = new ProductPutDTO();
        EntityNotFoundException exception = assertThrowsExactly(EntityNotFoundException.class,
                () -> productService.update(putDTO, 0));
        assertEquals("Produto não encontrado!", exception.getMessage());
    }

    @Test
    void delete() {
        Integer id = productRepository.save(generateRandomProduct()).getId();
        assertEquals(1, productRepository.findAll().size());
        assertDoesNotThrow(() -> productService.delete(id));
        assertEquals(0, productRepository.findAll().size());
    }

    @Test
    void delete_error_notFound() {
        Integer id = productRepository.save(generateRandomProduct()).getId();
        assertEquals(1, productRepository.findAll().size());
        EntityNotFoundException exception = assertThrowsExactly(EntityNotFoundException.class,
                () -> productService.delete(id + 1));
        assertEquals("Produto não encontrado!", exception.getMessage());
    }

}
