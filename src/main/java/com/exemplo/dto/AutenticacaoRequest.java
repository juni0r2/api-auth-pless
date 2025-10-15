package com.exemplo.dto;

import com.exemplo.model.TipoContato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutenticacaoRequest {
    
    @NotBlank(message = "Vertical é obrigatória")
    private String vertical;
    
    @NotBlank(message = "Jornada é obrigatória")
    private String jornada;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
    private String cpf;
    
    @NotNull(message = "Tipo de contato é obrigatório")
    private TipoContato tipoContato;
}

