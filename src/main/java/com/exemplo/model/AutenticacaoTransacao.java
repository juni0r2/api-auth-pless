package com.exemplo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "autenticacao_transacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutenticacaoTransacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "identificador", nullable = false, length = 50)
    private String identificador;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "token", nullable = false, length = 6)
    private String token;
    
    @Column(name = "vertical", nullable = false, length = 50)
    private String vertical;
    
    @Column(name = "jornada", nullable = false, length = 50)
    private String jornada;
    
    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contato", nullable = false)
    private TipoContato tipoContato;
    
    @Column(name = "utilizado", nullable = false)
    private Boolean utilizado = false;
    
    @Column(name = "data_utilizacao")
    private LocalDateTime dataUtilizacao;
    
    public AutenticacaoTransacao(String identificador, String token, String vertical, String jornada, String cpf, TipoContato tipoContato) {
        this.identificador = identificador;
        this.token = token;
        this.vertical = vertical;
        this.jornada = jornada;
        this.cpf = cpf;
        this.tipoContato = tipoContato;
        this.dataCriacao = LocalDateTime.now();
        this.utilizado = false;
    }
}

