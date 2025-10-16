package com.exemplo.controller;

import com.exemplo.service.AutenticacaoService;
import com.exemplo.service.OAuthService;
import com.exemplo.dto.OAuthTokenResponse;
import com.exemplo.dto.ValidacaoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 🔐 Controller OAuth 2.0 para Passwordless Authentication
 * 
 * Este controller implementa endpoints OAuth 2.0 padrão
 * usando grant type personalizado "passwordless"
 */
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuthPasswordlessController {

    private final AutenticacaoService autenticacaoService;
    private final OAuthService oauthService;

    /**
     * 🎫 Endpoint OAuth 2.0 padrão com grant type passwordless
     * 
     * Este endpoint segue o padrão OAuth 2.0 mas usa autenticação passwordless
     * 
     * @param grantType Tipo de grant (deve ser "passwordless")
     * @param cpf CPF do usuário
     * @param vertical Vertical do usuário
     * @param jornada Jornada do usuário
     * @param token Token de 6 dígitos
     * @param scope Escopo do token
     * @return Token OAuth 2.0
     */
    @PostMapping("/token")
    public ResponseEntity<OAuthTokenResponse> token(
            @RequestParam("grant_type") String grantType,
            @RequestParam("cpf") String cpf,
            @RequestParam("vertical") String vertical,
            @RequestParam("jornada") String jornada,
            @RequestParam("token") String token,
            @RequestParam(value = "scope", defaultValue = "read write") String scope) {

        try {
            // Validar grant type
            if (!"passwordless".equals(grantType)) {
                OAuthTokenResponse errorResponse = new OAuthTokenResponse();
                errorResponse.setError("unsupported_grant_type");
                errorResponse.setErrorDescription("Grant type deve ser 'passwordless'");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Validar o token de 6 dígitos
            String identificador = cpf + "|" + vertical + "|" + jornada;
            ValidacaoResponse validacaoResponse = autenticacaoService.validarToken(identificador, token);
            
            if (validacaoResponse == null || !validacaoResponse.getMensagem().contains("válido")) {
                OAuthTokenResponse errorResponse = new OAuthTokenResponse();
                errorResponse.setError("invalid_grant");
                errorResponse.setErrorDescription("Token inválido ou expirado");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Gerar token OAuth usando passwordless
            var oauthRequest = new com.exemplo.dto.OAuthTokenRequest();
            oauthRequest.setGrantType("passwordless");
            oauthRequest.setClientId("api-auth-pless-client");
            oauthRequest.setClientSecret("secret");
            oauthRequest.setScope(scope);
            oauthRequest.setUsername(cpf);
            oauthRequest.setPassword("passwordless");

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
     * 🔍 Endpoint para validar token OAuth
     * 
     * @param token Token a ser validado
     * @return Status do token
     */
    @PostMapping("/introspect")
    public ResponseEntity<Map<String, Object>> introspect(@RequestParam("token") String token) {
        try {
            boolean isValid = oauthService.validateToken(token);
            
            Map<String, Object> response = Map.of(
                "active", isValid,
                "token_type", "Bearer",
                "scope", "read write"
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "active", false,
                "error", "server_error",
                "error_description", "Erro interno: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
