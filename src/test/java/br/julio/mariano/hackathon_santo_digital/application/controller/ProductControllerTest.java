package br.julio.mariano.hackathon_santo_digital.application.controller;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ProductColour;
import org.openapitools.model.ProductPostDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.julio.mariano.hackathon_santo_digital.config.TestContext;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.ProductRepository;

class ProductControllerTest extends TestContext {

    private final ProductRepository productRepository;
    private final String baseEndpoint = "/products";

    public ProductControllerTest(ObjectMapper objectMapper, MockMvc mockMvc, ProductRepository productRepository) {
        super(objectMapper, mockMvc);
        this.productRepository = productRepository;
    }

    @BeforeEach
    void beforeEach() {
        productRepository.deleteAll();
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

}
