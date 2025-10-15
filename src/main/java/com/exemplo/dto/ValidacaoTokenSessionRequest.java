package com.exemplo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para request de validação do token de sessão.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacaoTokenSessionRequest {
    
    @NotBlank(message = "Token de sessão é obrigatório")
    private String tokenSessionId;
}

