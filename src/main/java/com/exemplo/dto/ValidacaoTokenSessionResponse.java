package com.exemplo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para response de validação do token de sessão.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacaoTokenSessionResponse {
    
    private boolean valido;
    private String cpf;
    private String vertical;
    private String jornada;
    private LocalDateTime dataValidacao;
    private String mensagem;
    
    public ValidacaoTokenSessionResponse(boolean valido, String mensagem) {
        this.valido = valido;
        this.mensagem = mensagem;
    }
}

