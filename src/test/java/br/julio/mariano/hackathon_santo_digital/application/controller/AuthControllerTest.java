package br.julio.mariano.hackathon_santo_digital.application.controller;

import java.util.Collections;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.openapitools.model.RoleEnum;
import org.openapitools.model.SigninForm;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.julio.mariano.hackathon_santo_digital.config.TestContext;
import br.julio.mariano.hackathon_santo_digital.domain.model.Role;
import br.julio.mariano.hackathon_santo_digital.domain.model.User;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.UserRepository;

public class AuthControllerTest extends TestContext {

        private final UserRepository userRepository;
        private final String urlLogin = "/login";

        public AuthControllerTest(ObjectMapper objectMapper, MockMvc mockMvc, UserRepository userRepository) {
                super(objectMapper, mockMvc);
                this.userRepository = userRepository;
        }

        @Test
        void login() throws Exception {
                Role adminRole = Role.builder().id(Short.valueOf("1")).name("ADMIN").build();
                String username = getRandomString() + "@test.com";
                String password = getRandomString(10);
                userRepository.register(new User(username, password, Collections.singleton(adminRole)));
                SigninForm signinForm = new SigninForm(username, password);
                getMockMvc().perform(
                                MockMvcRequestBuilders.post(urlLogin)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(writeValueAsString(signinForm)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0]").value("ADMIN"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.JWT").isNotEmpty());
        }

        @Test
        void login_error_invalidSigninForm() throws Exception {
                SigninForm signinForm = new SigninForm(getRandomString(), getRandomString(5));
                getMockMvc().perform(
                                MockMvcRequestBuilders.post(urlLogin)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(writeValueAsString(signinForm)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Input validation error!"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors", Matchers.hasSize(2)));
        }

        @Test
        void login_error_badCredentials() throws Exception {
                SigninForm signinForm = new SigninForm("email@nao.existe", getRandomString());
                getMockMvc().perform(
                                MockMvcRequestBuilders.post(urlLogin)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(writeValueAsString(signinForm)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty());
        }
}
