package com.exemplo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Registro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(min = 11, max = 11, message = "CPF deve conter exatamente 11 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "CPF deve conter apenas números")
    private String cpf;
    
    private String vertical;
    private String jornada;
    
    @Column(unique = true)
    @Email(message = "Email deve ser válido")
    private String email;
    
    private String telefone;
    
    private LocalDateTime dataCriacao;
    private boolean ativo;
    private String identificador; // concatenação de vertical|jornada|cpf
    
    // Construtor padrão
    public Registro() {}
    
    // Construtor com os três parâmetros principais
    public Registro(String cpf, String vertical, String jornada) {
        this.cpf = cpf;
        this.vertical = vertical;
        this.jornada = jornada;
    }
    
    @PrePersist
    @PreUpdate
    public void gerarIdentificador() {
        // Substituir espaços por hifens e converter para lowercase
        String verticalFormatada = vertical != null ? vertical.replace(" ", "-").toLowerCase() : "";
        String jornadaFormatada = jornada != null ? jornada.replace(" ", "-").toLowerCase() : "";
        String cpfFormatado = cpf != null ? cpf : "";
        
        // Concatenar na ordem especificada: vertical|jornada|cpf
        this.identificador = verticalFormatada + "|" + jornadaFormatada + "|" + cpfFormatado;
        
        // Se for uma nova entidade, definir a data de criação
        if (dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
    }
}