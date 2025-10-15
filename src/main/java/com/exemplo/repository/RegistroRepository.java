package com.exemplo.repository;

import com.exemplo.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    // Método para buscar registro por identificador
    Optional<Registro> findByIdentificador(String identificador);
}