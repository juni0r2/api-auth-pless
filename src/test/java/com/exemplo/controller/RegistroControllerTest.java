package com.exemplo.controller;

import com.exemplo.dto.RegistroRequest;
import com.exemplo.model.Registro;
import com.exemplo.service.RegistroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistroController.class)
@DisplayName("Testes do RegistroController")
class RegistroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistroService registroService;

    @Autowired
    private ObjectMapper objectMapper;

    private Registro registro;
    private RegistroRequest registroRequest;

    @BeforeEach
    void setUp() {
        registro = new Registro("12345678901", "SAUDE", "DIGITAL");
        registro.setId(1L);
        registro.setEmail("teste@exemplo.com");
        registro.setTelefone("11999887766");
        registro.setDataCriacao(LocalDateTime.now());
        registro.setAtivo(true);
        registro.gerarIdentificador();

        registroRequest = new RegistroRequest();
        registroRequest.setCpf("12345678901");
        registroRequest.setVertical("SAUDE");
        registroRequest.setJornada("DIGITAL");
        registroRequest.setEmail("teste@exemplo.com");
        registroRequest.setTelefone("11999887766");
        registroRequest.setAtivo(true);
    }

    @Test
    @DisplayName("Deve listar todos os registros")
    void deveListarTodosOsRegistros() throws Exception {
        // Given
        List<Registro> registros = Arrays.asList(registro);
        when(registroService.listarTodos()).thenReturn(registros);

        // When & Then
        mockMvc.perform(get("/api/registros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cpf").value("12345678901"))
                .andExpect(jsonPath("$[0].vertical").value("SAUDE"))
                .andExpect(jsonPath("$[0].jornada").value("DIGITAL"));
    }

    @Test
    @DisplayName("Deve buscar registro por ID quando existir")
    void deveBuscarRegistroPorIdQuandoExistir() throws Exception {
        // Given
        when(registroService.buscarPorId(1L)).thenReturn(Optional.of(registro));

        // When & Then
        mockMvc.perform(get("/api/registros/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.vertical").value("SAUDE"))
                .andExpect(jsonPath("$.jornada").value("DIGITAL"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando buscar por ID inexistente")
    void deveRetornar404QuandoBuscarPorIdInexistente() throws Exception {
        // Given
        when(registroService.buscarPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/registros/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve criar novo registro")
    void deveCriarNovoRegistro() throws Exception {
        // Given
        when(registroService.salvar(any(Registro.class))).thenReturn(registro);

        // When & Then
        mockMvc.perform(post("/api/registros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.vertical").value("SAUDE"))
                .andExpect(jsonPath("$.jornada").value("DIGITAL"));
    }

    @Test
    @DisplayName("Deve atualizar registro existente")
    void deveAtualizarRegistroExistente() throws Exception {
        // Given
        Registro registroAtualizado = new Registro("12345678901", "SAUDE", "DIGITAL");
        registroAtualizado.setId(1L);
        registroAtualizado.setEmail("novo@exemplo.com");
        registroAtualizado.setTelefone("11999887766");
        registroAtualizado.setAtivo(true);

        when(registroService.buscarPorId(1L)).thenReturn(Optional.of(registro));
        when(registroService.salvar(any(Registro.class))).thenReturn(registroAtualizado);

        // When & Then
        mockMvc.perform(put("/api/registros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("novo@exemplo.com"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar registro inexistente")
    void deveRetornar404AoTentarAtualizarRegistroInexistente() throws Exception {
        // Given
        when(registroService.buscarPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/registros/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve excluir registro existente")
    void deveExcluirRegistroExistente() throws Exception {
        // Given
        when(registroService.buscarPorId(1L)).thenReturn(Optional.of(registro));

        // When & Then
        mockMvc.perform(delete("/api/registros/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar excluir registro inexistente")
    void deveRetornar404AoTentarExcluirRegistroInexistente() throws Exception {
        // Given
        when(registroService.buscarPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/registros/999"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Deve aceitar dados válidos")
    void deveAceitarDadosValidos() throws Exception {
        // Given
        RegistroRequest requestValido = new RegistroRequest();
        requestValido.setCpf("12345678901"); // CPF válido
        requestValido.setVertical("SAUDE");
        requestValido.setJornada("DIGITAL");
        requestValido.setEmail("teste@exemplo.com"); // Email válido
        requestValido.setAtivo(true);

        when(registroService.salvar(any(Registro.class))).thenReturn(registro);

        // When & Then
        mockMvc.perform(post("/api/registros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isCreated());
    }
}
