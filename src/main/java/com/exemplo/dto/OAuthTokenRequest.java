package com.exemplo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 📝 DTO para requisição de token OAuth
 */
@Data
public class OAuthTokenRequest {
    
    @NotBlank(message = "Grant type é obrigatório")
    private String grantType;
    
    @NotBlank(message = "Client ID é obrigatório")
    private String clientId;
    
    @NotBlank(message = "Client Secret é obrigatório")
    private String clientSecret;
    
    private String username;
    private String password;
    private String scope;
    private String redirectUri;
    private String code;
    private String refreshToken;
}
