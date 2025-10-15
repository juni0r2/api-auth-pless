package com.exemplo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 📝 DTO para resposta de token OAuth
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuthTokenResponse {
    
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String refreshToken;
    private String scope;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private String error;
    private String errorDescription;
}
