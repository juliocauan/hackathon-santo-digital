package br.julio.mariano.hackathon_santo_digital.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private final List<Product> createdProducts = new ArrayList<>();

    public ProductServiceTest(ObjectMapper objectMapper, MockMvc mockMvc, ProductRepository productRepository,
            ProductService productService) {
        super(objectMapper, mockMvc);
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void beforeEach() {
        productRepository.deleteAll(createdProducts);
        createdProducts.clear();
    }

    private final Product generateRandomProduct() {
        return Product.builder()
                .id(null)
                .color(getRandomString(10))
                .daysToManufacture(getRandomInteger())
                .listPrice(getRandomBigDecimal())
                .name(getRandomString())
                .productNumber(getRandomString(10))
                .reorderPoint(Short.valueOf("750"))
                .safetyStockLevel(Short.valueOf("1000"))
                .sellStartDate(LocalDate.now())
                .standardCost(new BigDecimal(Math.round(Math.random() * 100)))
                .build();
    }

    @Test
    void register() {
        ProductPostDTO expectedProduct = new ProductPostDTO(getRandomString(), getRandomString(15), getRandomInteger(),
                getRandomInteger(), getRandomBigDecimal(), getRandomBigDecimal(), getRandomInteger(), LocalDate.now())
                .color(getRandomString(10));
        Product fetchedProduct;
        assertDoesNotThrow(() -> productService.register(expectedProduct));
        fetchedProduct = productRepository.findAll().get(0);
        assertEquals(expectedProduct.getColor(), fetchedProduct.getColor());
        assertEquals(expectedProduct.getName(), fetchedProduct.getName());
        assertEquals(expectedProduct.getProductNumber(), fetchedProduct.getProductNumber());
        assertEquals(expectedProduct.getStandardCost(), fetchedProduct.getStandardCost());
        assertEquals(expectedProduct.getDaysToManufacture(), fetchedProduct.getDaysToManufacture());
        assertEquals(expectedProduct.getListPrice(), fetchedProduct.getListPrice());
        assertEquals(expectedProduct.getReorderPoint(), fetchedProduct.getReorderPoint());
        assertEquals(expectedProduct.getSafetyStockLevel(), fetchedProduct.getSafetyStockLevel());
        assertEquals(expectedProduct.getSellStartDate(), fetchedProduct.getSellStartDate());
    }

    @Test
    void update() {
        Product oldProduct = productRepository.saveAndFlush(generateRandomProduct());
        Product newProduct;
        ProductPutDTO putDTO = new ProductPutDTO()
                .color(getRandomString(10))
                .daysToManufacture(getRandomInteger())
                .reorderPoint(getRandomInteger())
                .standardCost(getRandomBigDecimal());
        assertDoesNotThrow(() -> productService.update(putDTO, oldProduct.getId()));
        newProduct = productRepository.findAll().get(0);
        assertEquals(oldProduct.getId(), newProduct.getId());
        assertEquals(putDTO.getColor(), newProduct.getColor());
        assertEquals(oldProduct.getName(), newProduct.getName());
        assertEquals(oldProduct.getProductNumber(), newProduct.getProductNumber());
        assertEquals(putDTO.getStandardCost(), newProduct.getStandardCost());
        assertEquals(putDTO.getDaysToManufacture(), newProduct.getDaysToManufacture());
        assertEquals(oldProduct.getListPrice(), newProduct.getListPrice());
        assertEquals(putDTO.getReorderPoint(), newProduct.getReorderPoint());
        assertEquals(oldProduct.getSafetyStockLevel(), newProduct.getSafetyStockLevel());
        assertEquals(oldProduct.getSellStartDate(), newProduct.getSellStartDate());
        productRepository.delete(oldProduct);
    }

    @Test
    void update_branch_withNullParams() {
        Product oldProduct = productRepository.saveAndFlush(generateRandomProduct());
        Product newProduct;
        ProductPutDTO putDTO = new ProductPutDTO()
                .color(null)
                .daysToManufacture(getRandomInteger())
                .reorderPoint(null)
                .standardCost(getRandomBigDecimal());
        assertDoesNotThrow(() -> productService.update(putDTO, oldProduct.getId()));
        newProduct = productRepository.findAll().get(0);
        assertEquals(oldProduct.getId(), newProduct.getId());
        assertEquals(putDTO.getColor(), newProduct.getColor());
        assertEquals(oldProduct.getName(), newProduct.getName());
        assertEquals(oldProduct.getProductNumber(), newProduct.getProductNumber());
        assertEquals(putDTO.getStandardCost(), newProduct.getStandardCost());
        assertEquals(putDTO.getDaysToManufacture(), newProduct.getDaysToManufacture());
        assertEquals(oldProduct.getListPrice(), newProduct.getListPrice());
        assertEquals(oldProduct.getReorderPoint(), newProduct.getReorderPoint());
        assertEquals(oldProduct.getSafetyStockLevel(), newProduct.getSafetyStockLevel());
        assertEquals(oldProduct.getSellStartDate(), newProduct.getSellStartDate());
        productRepository.delete(oldProduct);
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
        assertTrue(productRepository.findById(id).isPresent());
        assertDoesNotThrow(() -> productService.delete(id));
        assertFalse(productRepository.findById(id).isPresent());
    }

    @Test
    void delete_error_notFound() {
        Integer id = productRepository.save(generateRandomProduct()).getId();
        assertTrue(productRepository.findById(id).isPresent());
        EntityNotFoundException exception = assertThrowsExactly(EntityNotFoundException.class,
                () -> productService.delete(id + 1));
        assertEquals("Produto não encontrado!", exception.getMessage());
        productRepository.deleteById(id);
    }

}
