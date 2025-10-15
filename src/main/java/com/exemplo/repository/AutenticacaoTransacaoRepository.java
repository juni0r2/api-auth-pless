package com.exemplo.repository;

import com.exemplo.model.AutenticacaoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutenticacaoTransacaoRepository extends JpaRepository<AutenticacaoTransacao, Long> {
    
    Optional<AutenticacaoTransacao> findByIdentificadorAndTokenAndUtilizadoFalse(String identificador, String token);
    
    Optional<AutenticacaoTransacao> findByIdentificadorAndUtilizadoFalse(String identificador);
}

