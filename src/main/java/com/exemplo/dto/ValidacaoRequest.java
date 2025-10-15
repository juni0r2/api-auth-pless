package com.exemplo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacaoRequest {
    
    @NotBlank(message = "{identificador.not.blank}")
    private String identificador;
    
    @NotBlank(message = "{token.not.blank}")
    private String token;
}

