package com.exemplo.controller;

import com.exemplo.dto.RegistroRequest;
import com.exemplo.model.Registro;
import com.exemplo.service.RegistroService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros")
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping
    public ResponseEntity<List<Registro>> listarTodos() {
        return ResponseEntity.ok(registroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Registro> buscarPorId(@PathVariable Long id) {
        return registroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Registro> criar(@RequestBody RegistroRequest registroRequest) {
        Registro registro = new Registro();
        BeanUtils.copyProperties(registroRequest, registro);
        // Copiar propriedade ativo manualmente devido à diferença entre Boolean e boolean
        if (registroRequest.getAtivo() != null) {
            registro.setAtivo(registroRequest.getAtivo());
        }
        Registro novoRegistro = registroService.salvar(registro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRegistro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Registro> atualizar(@PathVariable Long id, @RequestBody RegistroRequest registroRequest) {
        return registroService.buscarPorId(id)
                .map(registroExistente -> {
                    BeanUtils.copyProperties(registroRequest, registroExistente);
                    // Copiar propriedade ativo manualmente devido à diferença entre Boolean e boolean
                    if (registroRequest.getAtivo() != null) {
                        registroExistente.setAtivo(registroRequest.getAtivo());
                    }
                    registroExistente.setId(id);
                    return ResponseEntity.ok(registroService.salvar(registroExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return registroService.buscarPorId(id)
                .map(registro -> {
                    registroService.excluir(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
}