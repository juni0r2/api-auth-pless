package com.exemplo;

import com.exemplo.dto.IdentificadorRequest;
import com.exemplo.dto.RegistroRequest;
import com.exemplo.model.Registro;
import com.exemplo.repository.RegistroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DisplayName("Testes de Integração")
class RegistroIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RegistroRepository registroRepository;


    @BeforeEach
    void setUp() {
        registroRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar registro via repository")
    void deveCriarRegistroViaRepository() {
        // Given
        Registro registro = new Registro("12345678901", "SAUDE", "DIGITAL");
        registro.setEmail("teste@exemplo.com");
        registro.setTelefone("11999887766");
        registro.setAtivo(true);
        registro.gerarIdentificador();

        // When
        Registro salvo = registroRepository.save(registro);

        // Then
        assertNotNull(salvo.getId());
        assertEquals("12345678901", salvo.getCpf());
        assertEquals("SAUDE", salvo.getVertical());
        assertEquals("DIGITAL", salvo.getJornada());
        assertEquals("saude|digital|12345678901", salvo.getIdentificador());
    }

    @Test
    @DisplayName("Deve buscar registro por identificador")
    void deveBuscarRegistroPorIdentificador() {
        // Given
        Registro registro = new Registro("12345678901", "SAUDE", "DIGITAL");
        registro.setEmail("teste@exemplo.com");
        registro.setTelefone("11999887766");
        registro.setAtivo(true);
        registro.gerarIdentificador();
        registroRepository.save(registro);

        // When
        var resultado = registroRepository.findByIdentificador("saude|digital|12345678901");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("12345678901", resultado.get().getCpf());
        assertEquals("teste@exemplo.com", resultado.get().getEmail());
    }

    @Test
    @DisplayName("Deve retornar ProblemDetail quando registro não encontrado")
    void deveRetornarProblemDetailQuandoRegistroNaoEncontrado() {
        // Given
        IdentificadorRequest request = new IdentificadorRequest();
        request.setCpf("99999999999");
        request.setVertical("INEXISTENTE");
        request.setJornada("TESTE");

        // When
        ResponseEntity<?> response = restTemplate.postForEntity(
                "/api/autenticacao/valida-cpf",
                request,
                Object.class
        );

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
