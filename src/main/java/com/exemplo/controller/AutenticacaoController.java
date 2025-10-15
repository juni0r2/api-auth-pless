package com.exemplo.controller;

import com.exemplo.dto.AutenticacaoRequest;
import com.exemplo.dto.AutenticacaoResponse;
import com.exemplo.dto.IdentificadorRequest;
import com.exemplo.dto.IdentificadorResponse;
import com.exemplo.dto.ValidacaoRequest;
import com.exemplo.dto.ValidacaoResponse;
import com.exemplo.dto.ValidacaoTokenSessionRequest;
import com.exemplo.dto.ValidacaoTokenSessionResponse;
import com.exemplo.service.AutenticacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {
    
    private final AutenticacaoService autenticacaoService;
    private final MessageSource messageSource;
    
    @PostMapping
    public ResponseEntity<AutenticacaoResponse> autenticar(@Valid @RequestBody AutenticacaoRequest request) {
        AutenticacaoResponse response = autenticacaoService.gerarToken(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/valida-token")
    public ResponseEntity<ValidacaoResponse> validarToken(@Valid @RequestBody ValidacaoRequest request) {
        ValidacaoResponse response = autenticacaoService.validarToken(request.getIdentificador(), request.getToken());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/valida-cpf")
    public ResponseEntity<?> buscarPorIdentificador(@RequestBody IdentificadorRequest request) {
        return autenticacaoService.buscarPorIdentificadorComMascaramento(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    String title = messageSource.getMessage("error.cpf.not.found.title", null, LocaleContextHolder.getLocale());
                    String detail = messageSource.getMessage("error.cpf.not.found.detail", null, LocaleContextHolder.getLocale());
                    
                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                            HttpStatus.NOT_FOUND,
                            detail
                    );
                    problemDetail.setTitle(title);
                    return ResponseEntity.of(problemDetail).build();
                });
    }
    
    @PostMapping("/valida-token-session")
    public ResponseEntity<ValidacaoTokenSessionResponse> validarTokenSession(@Valid @RequestBody ValidacaoTokenSessionRequest request) {
        ValidacaoTokenSessionResponse response = autenticacaoService.validarTokenSession(request);
        return ResponseEntity.ok(response);
    }
    
}
