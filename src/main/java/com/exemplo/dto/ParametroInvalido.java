package com.exemplo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar um parâmetro inválido com campo e mensagem de erro.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametroInvalido {
    
    private String campo;
    private String mensagem;
}

