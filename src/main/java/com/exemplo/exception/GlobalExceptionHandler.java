package com.exemplo.exception;

import com.exemplo.dto.Problema;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, 
            WebRequest request) {
        
        String messageKey = "error.data.integrity.title";
        String detailKey = "error.data.integrity.detail";
        
        String errorMessage = ex.getMessage();
        if (errorMessage != null) {
            errorMessage = errorMessage.toLowerCase();
            
            // Verificar se é erro de email duplicado - mais robusto
            if (errorMessage.contains("email") || errorMessage.contains("uksvqlqo2hd1f1kxd6yyi2x9dtq")) {
                messageKey = "error.email.duplicate.title";
                detailKey = "error.email.duplicate.detail";
            } else if (errorMessage.contains("cpf")) {
                messageKey = "error.cpf.duplicate.title";
                detailKey = "error.cpf.duplicate.detail";
            }
        }
        
        String message = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
        String detail = messageSource.getMessage(detailKey, null, LocaleContextHolder.getLocale());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, 
                detail
        );
        
        problemDetail.setTitle(message);
        problemDetail.setType(URI.create("https://api.exemplo.com/errors/data-integrity-violation"));
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
            ConstraintViolationException ex, 
            WebRequest request) {
        
        String messageKey = "error.data.integrity.title";
        String detailKey = "error.data.integrity.detail";
        
        String errorMessage = ex.getMessage();
        if (errorMessage != null) {
            errorMessage = errorMessage.toLowerCase();
            
            // Verificar se é erro de email duplicado - mais robusto
            if (errorMessage.contains("email") || errorMessage.contains("uksvqlqo2hd1f1kxd6yyi2x9dtq")) {
                messageKey = "error.email.duplicate.title";
                detailKey = "error.email.duplicate.detail";
            } else if (errorMessage.contains("cpf")) {
                messageKey = "error.cpf.duplicate.title";
                detailKey = "error.cpf.duplicate.detail";
            }
        }
        
        String message = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
        String detail = messageSource.getMessage(detailKey, null, LocaleContextHolder.getLocale());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, 
                detail
        );
        
        problemDetail.setTitle(message);
        problemDetail.setType(URI.create("https://api.exemplo.com/errors/data-integrity-violation"));
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }
    
    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<Problema> handleTokenInvalidoException(TokenInvalidoException ex, WebRequest request) {
        Problema problema = new Problema(
            HttpStatus.BAD_REQUEST.value(),
            "TokenInvalido",
            "Token inválido",
            "Token inválido",
            "O token fornecido não existe no banco de dados ou já foi utilizado. Verifique se o token foi gerado corretamente e se não foi usado anteriormente. Tokens são válidos apenas uma vez."
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
    }
}
