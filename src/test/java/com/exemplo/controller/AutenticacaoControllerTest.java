package com.exemplo.controller;

import com.exemplo.dto.AutenticacaoRequest;
import com.exemplo.dto.AutenticacaoResponse;
import com.exemplo.dto.IdentificadorRequest;
import com.exemplo.dto.IdentificadorResponse;
import com.exemplo.dto.ValidacaoRequest;
import com.exemplo.dto.ValidacaoResponse;
import com.exemplo.dto.ValidacaoTokenSessionRequest;
import com.exemplo.dto.ValidacaoTokenSessionResponse;
import com.exemplo.exception.TokenInvalidoException;
import com.exemplo.model.TipoContato;
import com.exemplo.service.AutenticacaoService;
import com.exemplo.service.RegistroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutenticacaoController.class)
class AutenticacaoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AutenticacaoService autenticacaoService;
    
    @MockBean
    private RegistroService registroService;
    
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void deveGerarTokenComSucesso() throws Exception {
        // Given
        AutenticacaoRequest request = new AutenticacaoRequest();
        request.setVertical("Tecnologia");
        request.setJornada("Desenvolvimento");
        request.setCpf("12345678901");
        request.setTipoContato(TipoContato.EMAIL);
        
        AutenticacaoResponse response = new AutenticacaoResponse("tecnologia|desenvolvimento|12345678901", "123456");
        
        when(autenticacaoService.gerarToken(any(AutenticacaoRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/autenticacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identificador").value("tecnologia|desenvolvimento|12345678901"))
                .andExpect(jsonPath("$.token").value("123456"))
                .andExpect(jsonPath("$.mensagem").value("Token de autenticação gerado com sucesso"));
    }
    
    @Test
    void deveRetornarErroQuandoDadosInvalidos() throws Exception {
        // Given
        AutenticacaoRequest request = new AutenticacaoRequest();
        // CPF inválido (menos de 11 dígitos)
        request.setCpf("123");
        request.setVertical("Tecnologia");
        request.setJornada("Desenvolvimento");
        request.setTipoContato(TipoContato.EMAIL);
        
        // When & Then
        mockMvc.perform(post("/api/autenticacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void deveValidarTokenComSucesso() throws Exception {
        // Given
        ValidacaoRequest request = new ValidacaoRequest("tecnologia|desenvolvimento|12345678901", "123456");
        
        ValidacaoResponse response = new ValidacaoResponse("Token válido", "jwt-token-session-id");
        when(autenticacaoService.validarToken(request.getIdentificador(), request.getToken())).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/autenticacao/valida-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Token válido"))
                .andExpect(jsonPath("$.tokenSessionId").value("jwt-token-session-id"));
    }
    
    @Test
    void deveRetornarErroQuandoTokenInvalido() throws Exception {
        // Given
        ValidacaoRequest request = new ValidacaoRequest("tecnologia|desenvolvimento|12345678901", "999999");
        
        when(autenticacaoService.validarToken(request.getIdentificador(), request.getToken()))
            .thenThrow(new TokenInvalidoException("Token inválido"));
        
        // When & Then
        mockMvc.perform(post("/api/autenticacao/valida-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void deveBuscarPorIdentificadorQuandoRegistroExistir() throws Exception {
        // Given
        IdentificadorRequest request = new IdentificadorRequest();
        request.setCpf("12345678901");
        request.setVertical("SAUDE");
        request.setJornada("DIGITAL");
        
        IdentificadorResponse response = new IdentificadorResponse("teste@exemplo.com", "11999887766");
        
        when(registroService.buscarPorIdentificadorComMascaramento(any(IdentificadorRequest.class)))
                .thenReturn(java.util.Optional.of(response));
        
        // When & Then
        mockMvc.perform(post("/api/autenticacao/valida-cpf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("te***@exemplo.com"))
                .andExpect(jsonPath("$.telefone").value("11*******66"));
    }
    
    @Test
    void deveRetornarProblemDetailAoBuscarPorIdentificadorInexistente() throws Exception {
        // Given
        IdentificadorRequest request = new IdentificadorRequest();
        request.setCpf("99999999999");
        request.setVertical("INEXISTENTE");
        request.setJornada("TESTE");
        
        when(registroService.buscarPorIdentificadorComMascaramento(any(IdentificadorRequest.class)))
                .thenReturn(java.util.Optional.empty());
        
        // When & Then
        mockMvc.perform(post("/api/autenticacao/valida-cpf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").exists());
    }
    
    @Test
    void deveValidarTokenSessionComSucesso() throws Exception {
        // Given
        ValidacaoTokenSessionRequest request = new ValidacaoTokenSessionRequest("jwt-token-valid");
        ValidacaoTokenSessionResponse response = new ValidacaoTokenSessionResponse(
            true, "12345678901", "Tecnologia", "Desenvolvimento", 
            java.time.LocalDateTime.now(), "Token de sessão válido"
        );
        
        when(autenticacaoService.validarTokenSession(request)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/autenticacao/valida-token-session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valido").value(true))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.vertical").value("Tecnologia"))
                .andExpect(jsonPath("$.jornada").value("Desenvolvimento"))
                .andExpect(jsonPath("$.mensagem").value("Token de sessão válido"));
    }
    
    @Test
    void deveRetornarErroQuandoTokenSessionInvalido() throws Exception {
        // Given
        ValidacaoTokenSessionRequest request = new ValidacaoTokenSessionRequest("jwt-token-invalid");
        ValidacaoTokenSessionResponse response = new ValidacaoTokenSessionResponse(false, "Token de sessão inválido");
        
        when(autenticacaoService.validarTokenSession(request)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/autenticacao/valida-token-session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valido").value(false))
                .andExpect(jsonPath("$.mensagem").value("Token de sessão inválido"));
    }
    
    @Test
    void deveRetornarErroQuandoTokenSessionExpirado() throws Exception {
        // Given
        ValidacaoTokenSessionRequest request = new ValidacaoTokenSessionRequest("jwt-token-expired");
        ValidacaoTokenSessionResponse response = new ValidacaoTokenSessionResponse(false, "Token de sessão inválido");
        
        when(autenticacaoService.validarTokenSession(request)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/autenticacao/valida-token-session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valido").value(false))
                .andExpect(jsonPath("$.mensagem").value("Token de sessão inválido"));
    }
}
