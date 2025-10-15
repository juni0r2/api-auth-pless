package com.exemplo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para representar um problema com detalhes estruturados.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problema {
    
    private int status;
    private String tipo;
    private String titulo;
    private String detalhe;
    private String detalheTecnico;
    private List<ParametroInvalido> parametrosInvalidos;
    
    public Problema(int status, String tipo, String titulo, String detalhe) {
        this.status = status;
        this.tipo = tipo;
        this.titulo = titulo;
        this.detalhe = detalhe;
    }
    
    public Problema(int status, String tipo, String titulo, String detalhe, String detalheTecnico) {
        this.status = status;
        this.tipo = tipo;
        this.titulo = titulo;
        this.detalhe = detalhe;
        this.detalheTecnico = detalheTecnico;
    }
}
