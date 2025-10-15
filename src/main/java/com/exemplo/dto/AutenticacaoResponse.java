package com.exemplo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutenticacaoResponse {
    
    private String identificador;
    private String token;
    private String mensagem;
    
    public AutenticacaoResponse(String identificador, String token) {
        this.identificador = identificador;
        this.token = token;
        this.mensagem = "Token de autenticação gerado com sucesso";
    }
}

