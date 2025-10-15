package com.exemplo.service;

import com.exemplo.dto.AutenticacaoRequest;
import com.exemplo.dto.AutenticacaoResponse;
import com.exemplo.dto.ValidacaoResponse;
import com.exemplo.exception.TokenInvalidoException;
import com.exemplo.model.AutenticacaoTransacao;
import com.exemplo.model.TipoContato;
import com.exemplo.repository.AutenticacaoTransacaoRepository;
import com.exemplo.repository.RegistroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {
    
    @Mock
    private AutenticacaoTransacaoRepository autenticacaoTransacaoRepository;
    
    @Mock
    private RegistroRepository registroRepository;
    
    @Mock
    private JwtService jwtService;
    
    @InjectMocks
    private AutenticacaoService autenticacaoService;
    
    private AutenticacaoRequest request;
    
    @BeforeEach
    void setUp() {
        request = new AutenticacaoRequest();
        request.setVertical("Tecnologia");
        request.setJornada("Desenvolvimento");
        request.setCpf("12345678901");
        request.setTipoContato(TipoContato.EMAIL);
    }
    
    @Test
    void deveGerarTokenComSucesso() {
        // Given
        when(autenticacaoTransacaoRepository.findByIdentificadorAndUtilizadoFalse(anyString()))
            .thenReturn(Optional.empty());
        when(autenticacaoTransacaoRepository.save(any(AutenticacaoTransacao.class)))
            .thenReturn(new AutenticacaoTransacao());
        
        // When
        AutenticacaoResponse response = autenticacaoService.gerarToken(request);
        
        // Then
        assertNotNull(response);
        assertNotNull(response.getIdentificador());
        assertNotNull(response.getToken());
        assertEquals(6, response.getToken().length());
        assertTrue(response.getToken().matches("\\d{6}"));
        assertEquals("Token de autenticação gerado com sucesso", response.getMensagem());
        
        verify(autenticacaoTransacaoRepository).save(any(AutenticacaoTransacao.class));
    }
    
    @Test
    void deveRetornarTokenExistenteQuandoTransacaoAtiva() {
        // Given
        AutenticacaoTransacao transacaoExistente = new AutenticacaoTransacao();
        transacaoExistente.setIdentificador("tecnologia|desenvolvimento|12345678901");
        transacaoExistente.setToken("123456");
        transacaoExistente.setUtilizado(false);
        
        when(autenticacaoTransacaoRepository.findByIdentificadorAndUtilizadoFalse(anyString()))
            .thenReturn(Optional.of(transacaoExistente));
        
        // When
        AutenticacaoResponse response = autenticacaoService.gerarToken(request);
        
        // Then
        assertNotNull(response);
        assertEquals("tecnologia|desenvolvimento|12345678901", response.getIdentificador());
        assertEquals("123456", response.getToken());
        
        verify(autenticacaoTransacaoRepository, never()).save(any(AutenticacaoTransacao.class));
    }
    
    @Test
    void deveValidarTokenComSucesso() {
        // Given
        String identificador = "tecnologia|desenvolvimento|12345678901";
        String token = "123456";
        
        AutenticacaoTransacao transacao = new AutenticacaoTransacao();
        transacao.setIdentificador(identificador);
        transacao.setToken(token);
        transacao.setUtilizado(false);
        
        when(autenticacaoTransacaoRepository.findByIdentificadorAndTokenAndUtilizadoFalse(identificador, token))
            .thenReturn(Optional.of(transacao));
        when(autenticacaoTransacaoRepository.save(any(AutenticacaoTransacao.class)))
            .thenReturn(transacao);
        when(jwtService.gerarTokenSession(any(), any(), any(), any()))
            .thenReturn("jwt-token-session-id");
        
        // When
        ValidacaoResponse response = autenticacaoService.validarToken(identificador, token);
        
        // Then
        assertNotNull(response);
        assertEquals("Token válido", response.getMensagem());
        assertNotNull(response.getTokenSessionId());
        verify(autenticacaoTransacaoRepository).save(any(AutenticacaoTransacao.class));
    }
    
    @Test
    void deveLancarExcecaoQuandoTokenInvalido() {
        // Given
        String identificador = "tecnologia|desenvolvimento|12345678901";
        String token = "999999";
        
        when(autenticacaoTransacaoRepository.findByIdentificadorAndTokenAndUtilizadoFalse(identificador, token))
            .thenReturn(Optional.empty());
        
        // When & Then
        TokenInvalidoException exception = assertThrows(TokenInvalidoException.class, 
            () -> autenticacaoService.validarToken(identificador, token));
        
        // Verificar apenas que a exceção foi lançada
        assertNotNull(exception);
        verify(autenticacaoTransacaoRepository, never()).save(any(AutenticacaoTransacao.class));
    }
    
    @Test
    void deveGerarIdentificadorCorreto() {
        // Given
        when(autenticacaoTransacaoRepository.findByIdentificadorAndUtilizadoFalse(anyString()))
            .thenReturn(Optional.empty());
        when(autenticacaoTransacaoRepository.save(any(AutenticacaoTransacao.class)))
            .thenReturn(new AutenticacaoTransacao());
        
        // When
        AutenticacaoResponse response = autenticacaoService.gerarToken(request);
        
        // Then
        String expectedIdentificador = "tecnologia|desenvolvimento|12345678901";
        assertEquals(expectedIdentificador, response.getIdentificador());
    }
}
