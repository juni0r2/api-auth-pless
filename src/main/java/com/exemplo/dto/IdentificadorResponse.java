package com.exemplo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IdentificadorResponse {
    private String email;
    private String telefone;

    public IdentificadorResponse(String email, String telefone) {
        this.email = mascararEmail(email);
        this.telefone = mascararTelefone(telefone);
    }

    private String mascararEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email; // Se não tem @ ou tem apenas 1 caractere antes, retorna como está
        }
        
        String usuario = email.substring(0, atIndex);
        String dominio = email.substring(atIndex);
        
        // Mostra apenas os 2 primeiros caracteres do usuário
        String usuarioMascarado = usuario.substring(0, Math.min(2, usuario.length())) + 
                                 "***";
        
        return usuarioMascarado + dominio;
    }

    private String mascararTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return null;
        }
        
        // Remove caracteres não numéricos para processar
        String apenasNumeros = telefone.replaceAll("[^0-9]", "");
        
        if (apenasNumeros.length() < 4) {
            return telefone; // Se muito curto, retorna como está
        }
        
        // Mostra apenas os 2 primeiros e 2 últimos dígitos
        String inicio = apenasNumeros.substring(0, 2);
        String fim = apenasNumeros.substring(apenasNumeros.length() - 2);
        String meio = "*".repeat(apenasNumeros.length() - 4);
        
        return inicio + meio + fim;
    }
}