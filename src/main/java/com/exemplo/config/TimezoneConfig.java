package com.exemplo.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * Configuração de timezone para a aplicação.
 */
@Configuration
public class TimezoneConfig {

    @PostConstruct
    public void init() {
        // Define o timezone padrão da JVM para America/Sao_Paulo
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
