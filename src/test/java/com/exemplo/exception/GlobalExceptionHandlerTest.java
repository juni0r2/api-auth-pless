package com.exemplo.exception;

import com.exemplo.controller.RegistroController;
import com.exemplo.dto.RegistroRequest;
import com.exemplo.service.RegistroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistroController.class)
@TestPropertySource(properties = {"spring.messages.basename=messages"})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistroService registroService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private MessageSource messageSource;

    @Test
    void deveRetornarProblemDetailParaEmailDuplicado() throws Exception {
        // Arrange
        RegistroRequest request = new RegistroRequest();
        request.setCpf("12345678901");
        request.setEmail("email.duplicado@exemplo.com");
        request.setTelefone("11987654321");
        request.setVertical("Tecnologia");
        request.setJornada("Desenvolvimento");
        request.setAtivo(true);

        when(registroService.salvar(any()))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry 'email.duplicado@exemplo.com' for key 'UKsvqlqo2hd1f1kxd6yyi2x9dtq'"));

        // Act & Assert
        mockMvc.perform(post("/api/registros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.type").value("https://api.exemplo.com/errors/data-integrity-violation"));
    }

    @Test
    void deveRetornarProblemDetailParaCpfDuplicado() throws Exception {
        // Arrange
        RegistroRequest request = new RegistroRequest();
        request.setCpf("12345678901");
        request.setEmail("novo.email@exemplo.com");
        request.setTelefone("11987654321");
        request.setVertical("Tecnologia");
        request.setJornada("Desenvolvimento");
        request.setAtivo(true);

        when(registroService.salvar(any()))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry '12345678901' for key 'cpf'"));

        // Act & Assert
        mockMvc.perform(post("/api/registros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void deveRetornarProblemDetailGenericoParaOutrosErrosDeIntegridade() throws Exception {
        // Arrange
        RegistroRequest request = new RegistroRequest();
        request.setCpf("12345678901");
        request.setEmail("teste@exemplo.com");
        request.setTelefone("11987654321");
        request.setVertical("Tecnologia");
        request.setJornada("Desenvolvimento");
        request.setAtivo(true);

        when(registroService.salvar(any()))
                .thenThrow(new DataIntegrityViolationException("Generic integrity violation"));

        // Act & Assert
        mockMvc.perform(post("/api/registros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.status").value(409));
    }
}
