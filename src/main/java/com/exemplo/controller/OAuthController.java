package com.exemplo.controller;

import com.exemplo.dto.OAuthTokenRequest;
import com.exemplo.dto.OAuthTokenResponse;
import com.exemplo.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 🔐 Controller para gerenciamento de tokens OAuth 2.0
 * 
 * Este controller fornece endpoints para gerenciar tokens OAuth 2.0
 * usando o Spring Authorization Server.
 */
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oauthService;

    /**
     * 🎫 Endpoint para obter token de acesso
     * 
     * @param request Dados para geração do token
     * @return Token de acesso e informações relacionadas
     */
    @PostMapping("/token")
    public ResponseEntity<OAuthTokenResponse> getToken(@RequestBody OAuthTokenRequest request) {
        OAuthTokenResponse response = oauthService.generateToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 🔄 Endpoint para renovar token de acesso
     * 
     * @param refreshToken Token de renovação
     * @return Novo token de acesso
     */
    @PostMapping("/refresh")
    public ResponseEntity<OAuthTokenResponse> refreshToken(@RequestParam String refreshToken) {
        OAuthTokenResponse response = oauthService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    /**
     * ✅ Endpoint para validar token de acesso
     * 
     * @param token Token a ser validado
     * @return Status da validação
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = oauthService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    /**
     * 🗑️ Endpoint para revogar token
     * 
     * @param token Token a ser revogado
     * @return Confirmação da revogação
     */
    @PostMapping("/revoke")
    public ResponseEntity<String> revokeToken(@RequestParam String token) {
        oauthService.revokeToken(token);
        return ResponseEntity.ok("Token revogado com sucesso");
    }
}
