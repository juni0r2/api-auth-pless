package com.exemplo.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do IdentificadorResponse")
class IdentificadorResponseTest {

    @Test
    @DisplayName("Deve criar IdentificadorResponse com construtor padrão")
    void deveCriarIdentificadorResponseComConstrutorPadrao() {
        IdentificadorResponse response = new IdentificadorResponse();
        
        assertNotNull(response);
        assertNull(response.getEmail());
        assertNull(response.getTelefone());
    }

    @Test
    @DisplayName("Deve criar IdentificadorResponse com construtor parametrizado")
    void deveCriarIdentificadorResponseComConstrutorParametrizado() {
        String email = "teste@exemplo.com";
        String telefone = "11999887766";
        
        IdentificadorResponse response = new IdentificadorResponse(email, telefone);
        
        assertEquals("te***@exemplo.com", response.getEmail());
        assertEquals("11*******66", response.getTelefone());
    }

    @Test
    @DisplayName("Deve mascarar email corretamente")
    void deveMascararEmailCorretamente() {
        // Teste com email normal
        IdentificadorResponse response1 = new IdentificadorResponse("usuario@exemplo.com", "11999887766");
        assertEquals("us***@exemplo.com", response1.getEmail());

        // Teste com email curto
        IdentificadorResponse response2 = new IdentificadorResponse("ab@exemplo.com", "11999887766");
        assertEquals("ab***@exemplo.com", response2.getEmail());

        // Teste com email muito curto
        IdentificadorResponse response3 = new IdentificadorResponse("a@exemplo.com", "11999887766");
        assertEquals("a@exemplo.com", response3.getEmail());
    }

    @Test
    @DisplayName("Deve mascarar telefone corretamente")
    void deveMascararTelefoneCorretamente() {
        // Teste com telefone normal
        IdentificadorResponse response1 = new IdentificadorResponse("teste@exemplo.com", "11999887766");
        assertEquals("11*******66", response1.getTelefone());

        // Teste com telefone com formatação
        IdentificadorResponse response2 = new IdentificadorResponse("teste@exemplo.com", "(11) 99988-7766");
        assertEquals("11*******66", response2.getTelefone());

        // Teste com telefone curto
        IdentificadorResponse response3 = new IdentificadorResponse("teste@exemplo.com", "1199");
        assertEquals("1199", response3.getTelefone());
    }

    @Test
    @DisplayName("Deve lidar com email e telefone nulos")
    void deveLidarComEmailETelefoneNulos() {
        IdentificadorResponse response = new IdentificadorResponse(null, null);
        
        assertNull(response.getEmail());
        assertNull(response.getTelefone());
    }

    @Test
    @DisplayName("Deve lidar com email e telefone vazios")
    void deveLidarComEmailETelefoneVazios() {
        IdentificadorResponse response = new IdentificadorResponse("", "");
        
        assertNull(response.getEmail());
        assertNull(response.getTelefone());
    }

    @Test
    @DisplayName("Deve permitir setter e getter")
    void devePermitirSetterEGetter() {
        IdentificadorResponse response = new IdentificadorResponse();
        
        response.setEmail("teste@exemplo.com");
        response.setTelefone("11999887766");
        
        assertEquals("teste@exemplo.com", response.getEmail());
        assertEquals("11999887766", response.getTelefone());
    }

    @Test
    @DisplayName("Deve mascarar email com caracteres especiais")
    void deveMascararEmailComCaracteresEspeciais() {
        IdentificadorResponse response = new IdentificadorResponse("usuario.teste@exemplo.com", "11999887766");
        assertEquals("us***@exemplo.com", response.getEmail());
    }

    @Test
    @DisplayName("Deve mascarar telefone com diferentes formatos")
    void deveMascararTelefoneComDiferentesFormatos() {
        // Telefone com parênteses
        IdentificadorResponse response1 = new IdentificadorResponse("teste@exemplo.com", "(11) 99988-7766");
        assertEquals("11*******66", response1.getTelefone());

        // Telefone com hífens
        IdentificadorResponse response2 = new IdentificadorResponse("teste@exemplo.com", "11-99988-7766");
        assertEquals("11*******66", response2.getTelefone());

        // Telefone com espaços
        IdentificadorResponse response3 = new IdentificadorResponse("teste@exemplo.com", "11 99988 7766");
        assertEquals("11*******66", response3.getTelefone());
    }
}
