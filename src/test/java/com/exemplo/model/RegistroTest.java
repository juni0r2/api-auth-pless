package com.exemplo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Registro")
class RegistroTest {

    private Validator validator;
    private Registro registro;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        registro = new Registro();
    }

    @Test
    @DisplayName("Deve criar registro com construtor padrão")
    void deveCriarRegistroComConstrutorPadrao() {
        assertNotNull(registro);
        assertNull(registro.getCpf());
        assertNull(registro.getVertical());
        assertNull(registro.getJornada());
    }

    @Test
    @DisplayName("Deve criar registro com construtor parametrizado")
    void deveCriarRegistroComConstrutorParametrizado() {
        String cpf = "12345678901";
        String vertical = "SAUDE";
        String jornada = "DIGITAL";

        Registro novoRegistro = new Registro(cpf, vertical, jornada);

        assertEquals(cpf, novoRegistro.getCpf());
        assertEquals(vertical, novoRegistro.getVertical());
        assertEquals(jornada, novoRegistro.getJornada());
    }

    @Test
    @DisplayName("Deve gerar identificador corretamente")
    void deveGerarIdentificadorCorretamente() {
        registro.setCpf("12345678901");
        registro.setVertical("SAUDE");
        registro.setJornada("DIGITAL");

        registro.gerarIdentificador();

        assertEquals("saude|digital|12345678901", registro.getIdentificador());
    }

    @Test
    @DisplayName("Deve gerar identificador com espaços substituídos por hífens")
    void deveGerarIdentificadorComEspacosSubstituidos() {
        registro.setCpf("98765432100");
        registro.setVertical("TECNOLOGIA DA INFORMACAO");
        registro.setJornada("MANHA TARDE");

        registro.gerarIdentificador();

        assertEquals("tecnologia-da-informacao|manha-tarde|98765432100", registro.getIdentificador());
    }

    @Test
    @DisplayName("Deve definir data de criação na primeira execução")
    void deveDefinirDataCriacaoNaPrimeiraExecucao() {
        registro.setCpf("11111111111");
        registro.setVertical("TESTE");
        registro.setJornada("NOITE");

        assertNull(registro.getDataCriacao());

        registro.gerarIdentificador();

        assertNotNull(registro.getDataCriacao());
        assertTrue(registro.getDataCriacao().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Não deve alterar data de criação em atualizações")
    void naoDeveAlterarDataCriacaoEmAtualizacoes() {
        registro.setCpf("22222222222");
        registro.setVertical("TESTE");
        registro.setJornada("NOITE");

        registro.gerarIdentificador();
        LocalDateTime dataOriginal = registro.getDataCriacao();

        // Simular atualização
        registro.setEmail("teste@exemplo.com");
        registro.gerarIdentificador();

        assertEquals(dataOriginal, registro.getDataCriacao());
    }

    @Test
    @DisplayName("Deve validar CPF com exatamente 11 caracteres")
    void deveValidarCpfComExatamente11Caracteres() {
        registro.setCpf("1234567890"); // 10 caracteres
        registro.setVertical("TESTE");
        registro.setJornada("NOITE");

        Set<ConstraintViolation<Registro>> violations = validator.validate(registro);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("cpf")));
    }

    @Test
    @DisplayName("Deve validar CPF apenas com números")
    void deveValidarCpfApenasComNumeros() {
        registro.setCpf("1234567890a"); // contém letra
        registro.setVertical("TESTE");
        registro.setJornada("NOITE");

        Set<ConstraintViolation<Registro>> violations = validator.validate(registro);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("cpf")));
    }

    @Test
    @DisplayName("Deve validar email válido")
    void deveValidarEmailValido() {
        registro.setCpf("12345678901");
        registro.setVertical("TESTE");
        registro.setJornada("NOITE");
        registro.setEmail("teste@exemplo.com");

        Set<ConstraintViolation<Registro>> violations = validator.validate(registro);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar email inválido")
    void deveRejeitarEmailInvalido() {
        registro.setCpf("12345678901");
        registro.setVertical("TESTE");
        registro.setJornada("NOITE");
        registro.setEmail("email-invalido");

        Set<ConstraintViolation<Registro>> violations = validator.validate(registro);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Deve aceitar valores nulos para campos opcionais")
    void deveAceitarValoresNulosParaCamposOpcionais() {
        registro.setCpf("12345678901");
        registro.setVertical("TESTE");
        registro.setJornada("NOITE");
        registro.setEmail(null);
        registro.setTelefone(null);

        registro.gerarIdentificador();

        assertEquals("teste|noite|12345678901", registro.getIdentificador());
        assertNull(registro.getEmail());
        assertNull(registro.getTelefone());
    }
}
