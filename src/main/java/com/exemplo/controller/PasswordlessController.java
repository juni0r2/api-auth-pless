package com.exemplo.controller;

import com.exemplo.dto.AutenticacaoRequest;
import com.exemplo.dto.AutenticacaoResponse;
import com.exemplo.dto.OAuthTokenResponse;
import com.exemplo.dto.ValidacaoRequest;
import com.exemplo.dto.ValidacaoResponse;
import com.exemplo.service.AutenticacaoService;
import com.exemplo.service.OAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 🔐 Controller para autenticação passwordless
 * 
 * Este controller implementa o fluxo OAuth 2.0 personalizado
 * para autenticação sem senha usando tokens de 6 dígitos.
 */
@RestController
@RequestMapping("/api/passwordless")
@RequiredArgsConstructor
public class PasswordlessController {

    private final AutenticacaoService autenticacaoService;
    private final OAuthService oauthService;

    /**
     * 🎫 Endpoint para gerar token OAuth usando autenticação passwordless
     * 
     * @param request Dados da requisição (cpf, vertical, jornada, tipoContato, token)
     * @return Token OAuth 2.0
     */
    @PostMapping("/token")
    public ResponseEntity<OAuthTokenResponse> gerarTokenPasswordless(@Valid @RequestBody PasswordlessTokenRequest request) {
        try {
            // Validar o token de 6 dígitos
            String identificador = request.getCpf() + "|" + request.getVertical() + "|" + request.getJornada();

            // Verificar se o token é válido
            ValidacaoResponse validacaoResponse = autenticacaoService.validarToken(identificador, request.getToken());
            if (validacaoResponse == null || !validacaoResponse.getMensagem().contains("válido")) {
                OAuthTokenResponse errorResponse = new OAuthTokenResponse();
                errorResponse.setError("invalid_grant");
                errorResponse.setErrorDescription("Token inválido ou expirado");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Gerar token OAuth usando client_credentials
            com.exemplo.dto.OAuthTokenRequest oauthRequest = new com.exemplo.dto.OAuthTokenRequest();
            oauthRequest.setGrantType("client_credentials");
            oauthRequest.setClientId("api-auth-pless-client");
            oauthRequest.setClientSecret("secret");
            oauthRequest.setScope("read write");

            OAuthTokenResponse oauthResponse = oauthService.generateToken(oauthRequest);

            if (oauthResponse.getError() != null) {
                return ResponseEntity.badRequest().body(oauthResponse);
            }

            return ResponseEntity.ok(oauthResponse);

        } catch (Exception e) {
            OAuthTokenResponse errorResponse = new OAuthTokenResponse();
            errorResponse.setError("server_error");
            errorResponse.setErrorDescription("Erro interno: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 🔐 Endpoint OAuth 2.0 com Grant Type Passwordless
     * 
     * Este endpoint implementa o fluxo OAuth 2.0 padrão usando grant_type=passwordless
     * 
     * @param request Dados da requisição passwordless
     * @return Token OAuth 2.0 padrão
     */
    @PostMapping("/oauth2/token")
    public ResponseEntity<OAuthTokenResponse> gerarTokenOAuthPasswordless(@Valid @RequestBody PasswordlessTokenRequest request) {
        try {
            // Validar o token de 6 dígitos
            String identificador = request.getCpf() + "|" + request.getVertical() + "|" + request.getJornada();

            // Verificar se o token é válido
            ValidacaoResponse validacaoResponse = autenticacaoService.validarToken(identificador, request.getToken());
            if (validacaoResponse == null || !validacaoResponse.getMensagem().contains("válido")) {
                OAuthTokenResponse errorResponse = new OAuthTokenResponse();
                errorResponse.setError("invalid_grant");
                errorResponse.setErrorDescription("Token inválido ou expirado");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Gerar token OAuth usando passwordless (não client_credentials!)
            com.exemplo.dto.OAuthTokenRequest oauthRequest = new com.exemplo.dto.OAuthTokenRequest();
            oauthRequest.setGrantType("passwordless"); // Grant type personalizado
            oauthRequest.setClientId("api-auth-pless-client");
            oauthRequest.setClientSecret("secret");
            oauthRequest.setScope("read write");
            // Adicionar informações do usuário para o contexto
            oauthRequest.setUsername(request.getCpf());
            oauthRequest.setPassword("passwordless"); // Placeholder

            OAuthTokenResponse oauthResponse = oauthService.generateToken(oauthRequest);

            if (oauthResponse.getError() != null) {
                return ResponseEntity.badRequest().body(oauthResponse);
            }

            return ResponseEntity.ok(oauthResponse);

        } catch (Exception e) {
            OAuthTokenResponse errorResponse = new OAuthTokenResponse();
            errorResponse.setError("server_error");
            errorResponse.setErrorDescription("Erro interno: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 📝 DTO para requisição de token passwordless
     */
    public static class PasswordlessTokenRequest {
        private String cpf;
        private String vertical;
        private String jornada;
        private String tipoContato;
        private String token;

        // Getters e Setters
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getVertical() { return vertical; }
        public void setVertical(String vertical) { this.vertical = vertical; }
        
        public String getJornada() { return jornada; }
        public void setJornada(String jornada) { this.jornada = jornada; }
        
        public String getTipoContato() { return tipoContato; }
        public void setTipoContato(String tipoContato) { this.tipoContato = tipoContato; }
        
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
