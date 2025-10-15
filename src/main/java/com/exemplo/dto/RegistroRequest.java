package com.exemplo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Classe DTO que serve como fronteira da API para receber os dados de entrada
 */
@Data
public class RegistroRequest {
    
    @Size(min = 11, max = 11, message = "CPF deve conter exatamente 11 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "CPF deve conter apenas números")
    private String cpf;
    
    private String vertical;
    
    private String jornada;
    
    @Email(message = "Email deve ser válido")
    private String email;
    
    private String telefone;
    
    private Boolean ativo;
}