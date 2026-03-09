package br.dev.marcelosousa.userservice.rest.controller;

import br.dev.marcelosousa.userservice.config.SecurityConfigurations;
import br.dev.marcelosousa.userservice.dto.AuthDTO;
import br.dev.marcelosousa.userservice.dto.AuthResponseDTO;
import br.dev.marcelosousa.userservice.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfigurations.class)
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void authenticate_success() throws Exception {
        AuthDTO dto = new AuthDTO();
        dto.setEmail("user@example.com");
        dto.setPassword("password123");

        AuthResponseDTO response = new AuthResponseDTO();
        response.setName("John Doe");
        response.setToken("jwt-token-123");

        when(authenticationService.authenticate(any(AuthDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.token").value("jwt-token-123"));

        verify(authenticationService).authenticate(any(AuthDTO.class));
    }

    @Test
    void authenticate_failure_returns_unauthorized_with_message() throws Exception {
        AuthDTO dto = new AuthDTO();
        dto.setEmail("user@example.com");
        dto.setPassword("wrong-password");

        when(authenticationService.authenticate(any(AuthDTO.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void authenticate_validation_error_returns_bad_request() throws Exception {
        AuthDTO dto = new AuthDTO();
        dto.setEmail(""); // invalid: NotBlank + Email
        dto.setPassword(""); // invalid: NotBlank

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
