package br.julio.mariano.hackathon_santo_digital.application.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.OrderEnum;
import org.openapitools.model.ProductColour;
import org.openapitools.model.ProductFilter;
import org.openapitools.model.ProductPostDTO;
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

    public ProductControllerTest(ObjectMapper objectMapper, MockMvc mockMvc, ProductRepository productRepository) {
        super(objectMapper, mockMvc);
        this.productRepository = productRepository;
    }

    @BeforeEach
    void beforeEach() {
        productRepository.deleteAll();
        filter.colour(null).name(null).number(null).price(null);
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

    private final void saveSampleProducts(int quantity) {
        List<Product> samples = new ArrayList<>();
        for (int i = 0; i < quantity; i++)
            samples.add(generateRandomProduct());
        productRepository.saveAllAndFlush(samples);
    }

    @Test
    void registerProduct() throws Exception {
        ProductPostDTO postDTO = new ProductPostDTO(getRandomString(), 42, new BigDecimal("9.46"));
        getMockMvc().perform(MockMvcRequestBuilders.post(baseEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString(postDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Produto registrado com sucesso!"));
    }

    @Test
    void registerProduct_error_invalidInput() throws Exception {
        ProductPostDTO postDTO = new ProductPostDTO(null, null, null).colour(ProductColour.AZUL);
        getMockMvc().perform(MockMvcRequestBuilders.post(baseEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString(postDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Input validation error!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors", Matchers.hasSize(3)));
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
    void getAllProducts_branch_pageNumber() throws Exception {
        saveSampleProducts(sizeDefault + 1);
        getMockMvc().perform(
                MockMvcRequestBuilders.get(baseEndpoint)
                        .queryParam("page", "1")
                        .queryParam("size", sizeDefault.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    void getAllProducts_branch_pageSize() throws Exception {
        saveSampleProducts(sizeDefault);
        getMockMvc().perform(
                MockMvcRequestBuilders.get(baseEndpoint)
                        .queryParam("page", pageDefault.toString())
                        .queryParam("size", String.valueOf(sizeDefault - 1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault - 1)));
    }

    @Test
    void getAllProducts_branch_pageNumberAndSize() throws Exception {
        saveSampleProducts(28);
        getMockMvc().perform(
                MockMvcRequestBuilders.get(baseEndpoint)
                        .queryParam("page", "5")
                        .queryParam("size", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(expectedProduct.getName()));
    }

    @Test
    void getAllProducts_branch_orderByNameDesc() throws Exception {
        Product expectedProduct = generateRandomProduct();
        expectedProduct.setName("!00firstname");
        expectedProduct = productRepository.saveAndFlush(expectedProduct);
        saveSampleProducts(sizeDefault - 1);
        filter.name(OrderEnum.DESC);
        getMockMvc().perform(
                MockMvcRequestBuilders.get(baseEndpoint)
                        .queryParam("page", pageDefault.toString())
                        .queryParam("size", sizeDefault.toString())
                        .queryParam("name", filter.getName().getValue()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[4].name").value(expectedProduct.getName()));
    }

    @Test
    void getAllProducts_branch_orderByPriceAsc() throws Exception {
        Product expectedProduct = generateRandomProduct();
        expectedProduct.setPrice(new BigDecimal("0.01"));
        expectedProduct = productRepository.saveAndFlush(expectedProduct);
        saveSampleProducts(sizeDefault);
        filter.price(OrderEnum.ASC);
        getMockMvc().perform(
                MockMvcRequestBuilders.get(baseEndpoint)
                        .queryParam("page", pageDefault.toString())
                        .queryParam("size", sizeDefault.toString())
                        .queryParam("price", filter.getPrice().getValue()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].price").value(expectedProduct.getPrice()));
    }

    @Test
    void getAllProducts_branch_orderByPriceDesc() throws Exception {
        Product expectedProduct = generateRandomProduct();
        expectedProduct.setPrice(new BigDecimal("0.01"));
        expectedProduct = productRepository.saveAndFlush(expectedProduct);
        saveSampleProducts(sizeDefault - 1);
        filter.price(OrderEnum.DESC);
        getMockMvc().perform(
                MockMvcRequestBuilders.get(baseEndpoint)
                        .queryParam("page", pageDefault.toString())
                        .queryParam("size", sizeDefault.toString())
                        .queryParam("price", filter.getPrice().getValue()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(sizeDefault)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[4].price").value(expectedProduct.getPrice()));
    }

    @Test
    void getAllProducts_branch_filterByNumber() throws Exception {
        Product expectedProduct = productRepository.saveAndFlush(generateRandomProduct());
        saveSampleProducts(sizeDefault);
        filter.number(expectedProduct.getNumber());
        getMockMvc().perform(
                MockMvcRequestBuilders.get(baseEndpoint)
                        .queryParam("page", pageDefault.toString())
                        .queryParam("size", sizeDefault.toString())
                        .queryParam("number", filter.getNumber().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(expectedProduct.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].number").value(expectedProduct.getNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].price").value(expectedProduct.getPrice().intValue()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.[0].colour").value(expectedProduct.getColour().getValue()));
    }

    @Test
    void getAllProducts_branch_filterByColour() throws Exception {
        Product notInQueryProduct = generateRandomProduct();
        notInQueryProduct.setColour(ProductColour.VERMELHO);
        productRepository.saveAndFlush(notInQueryProduct);
        saveSampleProducts(3);
        filter.colour(ProductColour.AZUL);
        getMockMvc().perform(
                MockMvcRequestBuilders.get(baseEndpoint)
                        .queryParam("page", pageDefault.toString())
                        .queryParam("size", sizeDefault.toString())
                        .queryParam("colour", filter.getColour().getValue()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].colour").value(ProductColour.AZUL.getValue()));
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

    @Test
    void getProduct() throws Exception {
        Product expectedProduct = productRepository.saveAndFlush(generateRandomProduct());
        saveSampleProducts(sizeDefault);
        getMockMvc().perform(
                MockMvcRequestBuilders.get(baseEndpoint + "/{id}", expectedProduct.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedProduct.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedProduct.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(expectedProduct.getNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(expectedProduct.getPrice().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.colour").value(expectedProduct.getColour().getValue()));
    }

    @Test
    void getProduct_error_notFound() throws Exception {
        saveSampleProducts(sizeDefault + 1);
        getMockMvc().perform(MockMvcRequestBuilders.get(baseEndpoint + "/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Produto não encontrado!"));
    }

}
