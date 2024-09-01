package br.julio.mariano.hackathon_santo_digital.application.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.OrderEnum;
import org.openapitools.model.ProductFilter;
import org.openapitools.model.ProductPostDTO;
import org.openapitools.model.ProductPutDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.julio.mariano.hackathon_santo_digital.config.TestContext;
import br.julio.mariano.hackathon_santo_digital.domain.model.Product;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.ProductRepository;

class ProductControllerTest extends TestContext {

        private final ProductRepository productRepository;

        private final Integer pageDefault = 0;
        private final Integer sizeDefault = 5;
        private final ProductFilter filter = new ProductFilter();
        private final String baseEndpoint = "/products";
        private final List<Product> createdProducts = new ArrayList<>();

        public ProductControllerTest(ObjectMapper objectMapper, MockMvc mockMvc, ProductRepository productRepository) {
                super(objectMapper, mockMvc);
                this.productRepository = productRepository;
        }

        @BeforeEach
        void beforeEach() {
                productRepository.deleteAll(createdProducts);
                createdProducts.clear();
                filter.color(null).name(null).productNumber(null).standardCost(null);
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

        private final void saveSampleProducts(int quantity) {
                List<Product> samples = new ArrayList<>();
                for (int i = 0; i < quantity; i++)
                        samples.add(generateRandomProduct());
                createdProducts.addAll(productRepository.saveAllAndFlush(samples));
        }

        @Test
        void registerProduct() throws Exception {
                ProductPostDTO postDTO = new ProductPostDTO()
                                .color(getRandomString(10))
                                .daysToManufacture(getRandomInteger())
                                .listPrice(getRandomBigDecimal())
                                .name(getRandomString())
                                .productNumber(getRandomString(10))
                                .reorderPoint(getRandomInteger())
                                .safetyStockLevel(getRandomInteger())
                                .sellStartDate(LocalDate.now())
                                .standardCost(getRandomBigDecimal());
                getMockMvc().perform(MockMvcRequestBuilders.post(baseEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeValueAsString(postDTO)))
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Produto registrado com sucesso!"));
        }

        @Test
        void registerProduct_error_invalidInput() throws Exception {
                ProductPostDTO postDTO = new ProductPostDTO().color(getRandomString(10));
                getMockMvc().perform(MockMvcRequestBuilders.post(baseEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeValueAsString(postDTO)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Input validation error!"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors", Matchers.hasSize(8)));
        }

        @Test
        void getAllProducts() throws Exception {
                saveSampleProducts(sizeDefault + 1);
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint)
                                                .queryParam("page", pageDefault.toString())
                                                .queryParam("size", sizeDefault.toString()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault)));
        }

        @Test
        void getAllProducts_branch_orderByNameAsc() throws Exception {
                Product expectedProduct = generateRandomProduct();
                expectedProduct.setName("!00firstname");
                expectedProduct = productRepository.saveAndFlush(expectedProduct);
                saveSampleProducts(sizeDefault);
                filter.name(OrderEnum.ASC);
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint)
                                                .queryParam("page", pageDefault.toString())
                                                .queryParam("size", sizeDefault.toString())
                                                .queryParam("name", filter.getName().getValue()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name")
                                                .value(expectedProduct.getName()));
                productRepository.delete(expectedProduct);
        }

        @Test
        void getAllProducts_branch_orderByNameDesc() throws Exception {
                Product expectedProduct = generateRandomProduct();
                expectedProduct.setName("~99lastname");
                expectedProduct = productRepository.saveAndFlush(expectedProduct);
                filter.name(OrderEnum.DESC);
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint)
                                                .queryParam("page", pageDefault.toString())
                                                .queryParam("size", sizeDefault.toString())
                                                .queryParam("name", filter.getName().getValue()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name")
                                                .value(expectedProduct.getName()));
                productRepository.delete(expectedProduct);
        }

        @Test
        void getAllProducts_branch_orderByStandardCostAsc() throws Exception {
                filter.standardCost(OrderEnum.ASC);
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint)
                                                .queryParam("page", pageDefault.toString())
                                                .queryParam("size", sizeDefault.toString())
                                                .queryParam("standardCost", filter.getStandardCost().getValue()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].standardCost")
                                                .value(0));
        }

        @Test
        void getAllProducts_branch_orderByStandardCostDesc() throws Exception {
                Product expectedProduct = generateRandomProduct();
                expectedProduct.setStandardCost(new BigDecimal("3000.01"));
                expectedProduct = productRepository.saveAndFlush(expectedProduct);
                saveSampleProducts(sizeDefault - 1);
                filter.standardCost(OrderEnum.DESC);
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint)
                                                .queryParam("page", pageDefault.toString())
                                                .queryParam("size", sizeDefault.toString())
                                                .queryParam("standardCost", filter.getStandardCost().getValue()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].standardCost")
                                                .value(expectedProduct.getStandardCost()));
                productRepository.delete(expectedProduct);
        }

        @Test
        void getAllProducts_branch_filterByNumber() throws Exception {
                Product expectedProduct = productRepository.saveAndFlush(generateRandomProduct());
                saveSampleProducts(sizeDefault);
                filter.productNumber(expectedProduct.getProductNumber());
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint)
                                                .queryParam("page", pageDefault.toString())
                                                .queryParam("size", sizeDefault.toString())
                                                .queryParam("productNumber", filter.getProductNumber()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name")
                                                .value(expectedProduct.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].productNumber")
                                                .value(expectedProduct.getProductNumber()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].standardCost")
                                                .value(expectedProduct.getStandardCost().intValue()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].color")
                                                .value(expectedProduct.getColor()));
                productRepository.delete(expectedProduct);
        }

        @Test
        void getAllProducts_branch_filterByColor() throws Exception {
                Product expectedProduct = generateRandomProduct();
                expectedProduct.setColor(getRandomString(10));
                expectedProduct = productRepository.saveAndFlush(expectedProduct);
                saveSampleProducts(3);
                filter.color(expectedProduct.getColor());
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint)
                                                .queryParam("page", pageDefault.toString())
                                                .queryParam("size", sizeDefault.toString())
                                                .queryParam("color", filter.getColor()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].color")
                                                .value(expectedProduct.getColor()));
                productRepository.delete(expectedProduct);
        }

        @Test
        void getAllProducts_branch_nullPage() throws Exception {
                saveSampleProducts(20);
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint)
                                                .queryParam("size", sizeDefault.toString()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault)));
        }

        @Test
        void getAllProducts_branch_nullSize() throws Exception {
                saveSampleProducts(21);
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint)
                                                .queryParam("page", pageDefault.toString()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(20)));
        }

        @Test
        void getAllProducts_branch_nullParams() throws Exception {
                saveSampleProducts(21);
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(20)));
        }

        // TODO test more
        @Test
        void getProduct() throws Exception {
                Product expectedProduct = productRepository.saveAndFlush(generateRandomProduct());
                saveSampleProducts(sizeDefault);
                getMockMvc().perform(
                                MockMvcRequestBuilders.get(baseEndpoint + "/{id}", expectedProduct.getId()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedProduct.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedProduct.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productNumber")
                                                .value(expectedProduct.getProductNumber()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.standardCost")
                                                .value(expectedProduct.getStandardCost().intValue()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.color")
                                                .value(expectedProduct.getColor()));
                productRepository.delete(expectedProduct);
        }

        @Test
        void getProduct_error_notFound() throws Exception {
                saveSampleProducts(sizeDefault + 1);
                getMockMvc().perform(MockMvcRequestBuilders.get(baseEndpoint + "/{id}", 0))
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Produto não encontrado!"));
        }

        @Test
        void updateProduct() throws Exception {
                Product oldProduct = productRepository.saveAndFlush(generateRandomProduct());
                ProductPutDTO putDTO = new ProductPutDTO()
                                .color(getRandomString(10))
                                .daysToManufacture(getRandomInteger())
                                .reorderPoint(getRandomInteger())
                                .standardCost(getRandomBigDecimal());

                getMockMvc().perform(MockMvcRequestBuilders.put(baseEndpoint + "/{id}", oldProduct.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeValueAsString(putDTO)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Produto atualizado com sucesso!"));
                productRepository.delete(oldProduct);
        }

        @Test
        void updateProduct_branch_withNullParams() throws Exception {
                Product oldProduct = productRepository.saveAndFlush(generateRandomProduct());
                ProductPutDTO putDTO = new ProductPutDTO()
                                .color(null)
                                .daysToManufacture(null)
                                .reorderPoint(getRandomInteger())
                                .standardCost(getRandomBigDecimal());

                getMockMvc().perform(MockMvcRequestBuilders.put(baseEndpoint + "/{id}", oldProduct.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeValueAsString(putDTO)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Produto atualizado com sucesso!"));
                productRepository.delete(oldProduct);
        }

        @Test
        void updateProduct_error_invalidInput() throws Exception {
                Product oldProduct = productRepository.saveAndFlush(generateRandomProduct());
                ProductPutDTO putDTO = new ProductPutDTO()
                                .color(getRandomString(16))
                                .daysToManufacture(null)
                                .reorderPoint(getRandomInteger())
                                .standardCost(getRandomBigDecimal());

                getMockMvc().perform(MockMvcRequestBuilders.put(baseEndpoint + "/{id}", oldProduct.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeValueAsString(putDTO)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Input validation error!"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors", Matchers.hasSize(1)));
                productRepository.delete(oldProduct);
        }

        @Test
        void updateProduct_error_notFound() throws Exception {
                ProductPutDTO putDTO = new ProductPutDTO();
                getMockMvc().perform(MockMvcRequestBuilders.put(baseEndpoint + "/{id}", 0)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writeValueAsString(putDTO)))
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Produto não encontrado!"));
        }

        @Test
        void deleteProduct() throws Exception {
                Integer id = productRepository.saveAndFlush(generateRandomProduct()).getId();
                getMockMvc().perform(MockMvcRequestBuilders.delete(baseEndpoint + "/{id}", id))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Produto removido com sucesso!"));
        }

        @Test
        void deleteProduct_error_notFound() throws Exception {
                getMockMvc().perform(MockMvcRequestBuilders.delete(baseEndpoint + "/{id}", 0))
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Produto não encontrado!"));
        }

}
