package com.exemplo.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para receber os três atributos necessários para gerar um identificador
 */
@Data
public class IdentificadorRequest {
    
    @Size(min = 11, max = 11, message = "CPF deve conter exatamente 11 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "CPF deve conter apenas números")
    private String cpf;
    
    private String vertical;
    
    private String jornada;
}