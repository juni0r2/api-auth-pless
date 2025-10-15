package com.exemplo.exception;

/**
 * Exceção lançada quando um token de autenticação é inválido ou não existe no banco de dados.
 */
public class TokenInvalidoException extends RuntimeException {
    
    public TokenInvalidoException(String message) {
        super(message);
    }
}
