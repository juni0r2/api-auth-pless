package com.exemplo.config;

import com.exemplo.service.AutenticacaoService;
import com.exemplo.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 🔐 Handler para Grant Type Passwordless
 * 
 * Este componente implementa um fluxo OAuth 2.0 personalizado
 * para autenticação passwordless usando tokens de 6 dígitos.
 */
@Component
@RequiredArgsConstructor
public class PasswordlessGrantTypeHandler {

    private final AutenticacaoService autenticacaoService;
    private final OAuthService oauthService;

    /**
     * 🎫 Processa a requisição de token passwordless
     */
    public OAuth2AccessToken processPasswordlessToken(
            OAuth2ClientAuthenticationToken clientAuthentication,
            Map<String, Object> parameters,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {

        // Extrair parâmetros da requisição
        String cpf = (String) parameters.get("cpf");
        String vertical = (String) parameters.get("vertical");
        String jornada = (String) parameters.get("jornada");
        String tipoContato = (String) parameters.get("tipo_contato");
        String token = (String) parameters.get("token");

        // Validar parâmetros obrigatórios
        if (cpf == null || vertical == null || jornada == null || tipoContato == null || token == null) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_request", "Parâmetros obrigatórios: cpf, vertical, jornada, tipo_contato, token", null)
            );
        }

        try {
            // Validar o token de 6 dígitos usando nosso sistema existente
            String identificador = cpf + "|" + vertical + "|" + jornada;
            var validacaoResponse = autenticacaoService.validarToken(identificador, token);

            if (validacaoResponse == null || !validacaoResponse.getMensagem().contains("válido")) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_grant", "Token inválido ou expirado", null)
                );
            }

            // Gerar token OAuth usando client_credentials (mas com contexto de usuário)
            var oauthRequest = new com.exemplo.dto.OAuthTokenRequest();
            oauthRequest.setGrantType("client_credentials");
            oauthRequest.setClientId("api-auth-pless-client");
            oauthRequest.setClientSecret("secret");
            oauthRequest.setScope("read write");

            var oauthResponse = oauthService.generateToken(oauthRequest);

            if (oauthResponse.getError() != null) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("server_error", oauthResponse.getErrorDescription(), null)
                );
            }

            // Criar OAuth2AccessToken com informações do usuário
            return new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    oauthResponse.getAccessToken(),
                    oauthResponse.getIssuedAt() != null ? oauthResponse.getIssuedAt().atZone(java.time.ZoneId.systemDefault()).toInstant() : java.time.Instant.now(),
                    oauthResponse.getExpiresAt() != null ? oauthResponse.getExpiresAt().atZone(java.time.ZoneId.systemDefault()).toInstant() : java.time.Instant.now().plusSeconds(3600),
                    Set.of("read", "write")
            );

        } catch (Exception e) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("server_error", "Erro interno: " + e.getMessage(), null)
            );
        }
    }
}
