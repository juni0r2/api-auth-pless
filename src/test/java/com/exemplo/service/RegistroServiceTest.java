package com.exemplo.service;

import com.exemplo.dto.IdentificadorRequest;
import com.exemplo.dto.IdentificadorResponse;
import com.exemplo.model.Registro;
import com.exemplo.repository.RegistroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do RegistroService")
class RegistroServiceTest {

    @Mock
    private RegistroRepository registroRepository;

    @InjectMocks
    private RegistroService registroService;

    private Registro registro;
    private IdentificadorRequest identificadorRequest;

    @BeforeEach
    void setUp() {
        registro = new Registro("12345678901", "SAUDE", "DIGITAL");
        registro.setId(1L);
        registro.setEmail("teste@exemplo.com");
        registro.setTelefone("11999887766");
        registro.setDataCriacao(LocalDateTime.now());
        registro.setAtivo(true);
        registro.gerarIdentificador();

        identificadorRequest = new IdentificadorRequest();
        identificadorRequest.setCpf("12345678901");
        identificadorRequest.setVertical("SAUDE");
        identificadorRequest.setJornada("DIGITAL");
    }

    @Test
    @DisplayName("Deve listar todos os registros")
    void deveListarTodosOsRegistros() {
        // Given
        List<Registro> registros = Arrays.asList(registro, new Registro("98765432100", "TECNOLOGIA", "MANHA"));
        when(registroRepository.findAll()).thenReturn(registros);

        // When
        List<Registro> resultado = registroService.listarTodos();

        // Then
        assertEquals(2, resultado.size());
        verify(registroRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar registro por ID quando existir")
    void deveBuscarRegistroPorIdQuandoExistir() {
        // Given
        when(registroRepository.findById(1L)).thenReturn(Optional.of(registro));

        // When
        Optional<Registro> resultado = registroService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(registro, resultado.get());
        verify(registroRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar empty quando buscar por ID inexistente")
    void deveRetornarEmptyQuandoBuscarPorIdInexistente() {
        // Given
        when(registroRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Registro> resultado = registroService.buscarPorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(registroRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve buscar registro por identificador quando existir")
    void deveBuscarRegistroPorIdentificadorQuandoExistir() {
        // Given
        String identificador = "saude|digital|12345678901";
        when(registroRepository.findByIdentificador(identificador)).thenReturn(Optional.of(registro));

        // When
        Optional<Registro> resultado = registroService.buscarPorIdentificador(identificador);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(registro, resultado.get());
        verify(registroRepository).findByIdentificador(identificador);
    }

    @Test
    @DisplayName("Deve retornar empty quando buscar por identificador inexistente")
    void deveRetornarEmptyQuandoBuscarPorIdentificadorInexistente() {
        // Given
        String identificador = "inexistente|identificador|12345678901";
        when(registroRepository.findByIdentificador(identificador)).thenReturn(Optional.empty());

        // When
        Optional<Registro> resultado = registroService.buscarPorIdentificador(identificador);

        // Then
        assertFalse(resultado.isPresent());
        verify(registroRepository).findByIdentificador(identificador);
    }

    @Test
    @DisplayName("Deve salvar registro")
    void deveSalvarRegistro() {
        // Given
        Registro novoRegistro = new Registro("11111111111", "TESTE", "NOITE");
        when(registroRepository.save(any(Registro.class))).thenReturn(registro);

        // When
        Registro resultado = registroService.salvar(novoRegistro);

        // Then
        assertEquals(registro, resultado);
        verify(registroRepository).save(novoRegistro);
    }

    @Test
    @DisplayName("Deve excluir registro por ID")
    void deveExcluirRegistroPorId() {
        // Given
        Long id = 1L;
        doNothing().when(registroRepository).deleteById(id);

        // When
        registroService.excluir(id);

        // Then
        verify(registroRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve buscar por identificador com mascaramento quando registro existir")
    void deveBuscarPorIdentificadorComMascaramentoQuandoRegistroExistir() {
        // Given
        when(registroRepository.findByIdentificador("saude|digital|12345678901"))
                .thenReturn(Optional.of(registro));

        // When
        Optional<IdentificadorResponse> resultado = registroService.buscarPorIdentificadorComMascaramento(identificadorRequest);

        // Then
        assertTrue(resultado.isPresent());
        IdentificadorResponse response = resultado.get();
        assertEquals("te***@exemplo.com", response.getEmail());
        assertEquals("11*******66", response.getTelefone());
        verify(registroRepository).findByIdentificador("saude|digital|12345678901");
    }

    @Test
    @DisplayName("Deve retornar empty quando buscar por identificador com mascaramento e registro não existir")
    void deveRetornarEmptyQuandoBuscarPorIdentificadorComMascaramentoERegistroNaoExistir() {
        // Given
        when(registroRepository.findByIdentificador(anyString())).thenReturn(Optional.empty());

        // When
        Optional<IdentificadorResponse> resultado = registroService.buscarPorIdentificadorComMascaramento(identificadorRequest);

        // Then
        assertFalse(resultado.isPresent());
        verify(registroRepository).findByIdentificador("saude|digital|12345678901");
    }

    @Test
    @DisplayName("Deve mascarar email corretamente")
    void deveMascararEmailCorretamente() {
        // Given
        registro.setEmail("usuario@exemplo.com");
        when(registroRepository.findByIdentificador("saude|digital|12345678901"))
                .thenReturn(Optional.of(registro));

        // When
        Optional<IdentificadorResponse> resultado = registroService.buscarPorIdentificadorComMascaramento(identificadorRequest);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("us***@exemplo.com", resultado.get().getEmail());
    }

    @Test
    @DisplayName("Deve mascarar telefone corretamente")
    void deveMascararTelefoneCorretamente() {
        // Given
        registro.setTelefone("11987654321");
        when(registroRepository.findByIdentificador("saude|digital|12345678901"))
                .thenReturn(Optional.of(registro));

        // When
        Optional<IdentificadorResponse> resultado = registroService.buscarPorIdentificadorComMascaramento(identificadorRequest);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("11*******21", resultado.get().getTelefone());
    }

    @Test
    @DisplayName("Deve lidar com email e telefone nulos")
    void deveLidarComEmailETelefoneNulos() {
        // Given
        registro.setEmail(null);
        registro.setTelefone(null);
        when(registroRepository.findByIdentificador("saude|digital|12345678901"))
                .thenReturn(Optional.of(registro));

        // When
        Optional<IdentificadorResponse> resultado = registroService.buscarPorIdentificadorComMascaramento(identificadorRequest);

        // Then
        assertTrue(resultado.isPresent());
        assertNull(resultado.get().getEmail());
        assertNull(resultado.get().getTelefone());
    }
}
