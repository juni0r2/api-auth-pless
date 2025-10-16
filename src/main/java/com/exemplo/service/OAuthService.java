package com.exemplo.service;

import com.exemplo.dto.OAuthTokenRequest;
import com.exemplo.dto.OAuthTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * 🔐 Serviço para gerenciamento de tokens OAuth 2.0
 * 
 * Este serviço integra com o Spring Authorization Server
 * para gerenciar tokens de acesso de forma segura.
 */
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String TOKEN_ENDPOINT = "http://localhost:8080/oauth2/token";

    /**
     * 🎫 Gera token de acesso usando o Authorization Server
     * 
     * @param request Dados da requisição
     * @return Resposta com token
     */
    public OAuthTokenResponse generateToken(OAuthTokenRequest request) {
        try {
            // Preparar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(request.getClientId(), request.getClientSecret());

            // Preparar body da requisição
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", request.getGrantType());
            body.add("scope", request.getScope() != null ? request.getScope() : "read write");
            
            // Adicionar parâmetros específicos baseados no grant type
            switch (request.getGrantType()) {
                case "password" -> {
                    body.add("username", request.getUsername());
                    body.add("password", request.getPassword());
                }
                case "authorization_code" -> {
                    body.add("code", request.getCode());
                    body.add("redirect_uri", request.getRedirectUri());
                }
                case "refresh_token" -> {
                    body.add("refresh_token", request.getRefreshToken());
                }
                case "client_credentials" -> {
                    // Para client_credentials, não precisamos de parâmetros adicionais
                    // O client_id e client_secret já estão no header Authorization
                }
                case "passwordless" -> {
                    // Para passwordless, adicionar informações do usuário
                    body.add("username", request.getUsername());
                    body.add("password", request.getPassword());
                    // Adicionar parâmetros específicos do passwordless
                    body.add("cpf", request.getUsername()); // Usar CPF como username
                    body.add("vertical", "TECNOLOGIA"); // Pode ser extraído do contexto
                    body.add("jornada", "DESENVOLVIMENTO"); // Pode ser extraído do contexto
                }
            }

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            // Fazer requisição para o Authorization Server
            ResponseEntity<OAuthTokenResponse> response = restTemplate.exchange(
                    TOKEN_ENDPOINT,
                    HttpMethod.POST,
                    entity,
                    OAuthTokenResponse.class
            );

            OAuthTokenResponse tokenResponse = response.getBody();
            if (tokenResponse != null) {
                // Adicionar timestamps
                tokenResponse.setIssuedAt(LocalDateTime.now());
                if (tokenResponse.getExpiresIn() != null) {
                    tokenResponse.setExpiresAt(
                            LocalDateTime.now().plusSeconds(tokenResponse.getExpiresIn())
                    );
                }
            }

            return tokenResponse;

        } catch (Exception e) {
            OAuthTokenResponse errorResponse = new OAuthTokenResponse();
            errorResponse.setError("invalid_request");
            errorResponse.setErrorDescription("Erro ao gerar token: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * 🔄 Renova token de acesso usando refresh token
     * 
     * @param refreshToken Token de renovação
     * @return Novo token de acesso
     */
    public OAuthTokenResponse refreshToken(String refreshToken) {
        OAuthTokenRequest request = new OAuthTokenRequest();
        request.setGrantType("refresh_token");
        request.setRefreshToken(refreshToken);
        request.setClientId("api-auth-pless-client");
        request.setClientSecret("secret");
        
        return generateToken(request);
    }

    /**
     * ✅ Valida token de acesso
     * 
     * @param token Token a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean validateToken(String token) {
        try {
            // Fazer requisição para o endpoint de validação do Authorization Server
            String validateUrl = "http://localhost:8080/oauth2/introspect";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth("api-auth-pless-client", "secret");

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("token", token);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    validateUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 🗑️ Revoga token de acesso
     * 
     * @param token Token a ser revogado
     */
    public void revokeToken(String token) {
        try {
            String revokeUrl = "http://localhost:8080/oauth2/revoke";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth("api-auth-pless-client", "secret");

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("token", token);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            restTemplate.exchange(
                    revokeUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao revogar token: " + e.getMessage());
        }
    }
}
