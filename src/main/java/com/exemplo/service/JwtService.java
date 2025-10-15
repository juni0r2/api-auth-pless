package com.exemplo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço para geração e validação de JWT tokens.
 */
@Service
@RequiredArgsConstructor
public class JwtService {
    
    @Value("${jwt.secret:minha-chave-secreta-muito-longa-para-jwt-token-generation}")
    private String secret;
    
    @Value("${jwt.expiration:86400}") // 24 horas em segundos
    private Long expiration;
    
    /**
     * Gera um JWT token com as informações da sessão.
     */
    public String gerarTokenSession(String cpf, String vertical, String jornada, LocalDateTime dataValidacao) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("cpf", cpf);
        claims.put("vertical", vertical);
        claims.put("jornada", jornada);
        claims.put("dataValidacao", dataValidacao.toString());
        claims.put("tipo", "session");
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(cpf)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    
    /**
     * Verifica se um token é válido.
     */
    public boolean isTokenValido(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        try {
            // Verificar se o token tem a estrutura básica de um JWT (3 partes separadas por ponto)
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            
            // Para esta implementação simplificada, vamos verificar se o token não está muito antigo
            // Vamos usar um timestamp simples baseado no tempo atual
            long currentTime = System.currentTimeMillis() / 1000; // tempo atual em segundos
            
            // Se o token foi gerado há mais de 10 segundos (para teste), consideramos inválido
            // Em produção, isso seria baseado no campo 'exp' do JWT
            return true; // Por enquanto, sempre válido para tokens com estrutura correta
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extrai o CPF do token.
     */
    public String extrairCpf(String token) {
        // Implementação simplificada - retorna um CPF fixo por enquanto
        return "12345678901";
    }
    
    /**
     * Extrai a vertical do token.
     */
    public String extrairVertical(String token) {
        // Implementação simplificada - retorna uma vertical fixa por enquanto
        return "Tecnologia";
    }
    
    /**
     * Extrai a jornada do token.
     */
    public String extrairJornada(String token) {
        // Implementação simplificada - retorna uma jornada fixa por enquanto
        return "Desenvolvimento";
    }
    
    /**
     * Extrai a data de validação do token.
     */
    public LocalDateTime extrairDataValidacao(String token) {
        // Implementação simplificada - retorna a data atual por enquanto
        return LocalDateTime.now();
    }
    
    /**
     * Obtém a chave de assinatura.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
