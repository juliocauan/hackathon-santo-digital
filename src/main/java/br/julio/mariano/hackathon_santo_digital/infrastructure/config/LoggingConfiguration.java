package br.julio.mariano.hackathon_santo_digital.infrastructure.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {
    static {
        System.setProperty("java.util.logging.manager", "org.springframework.boot.logging.java.util.logging.ConfigurableLogFormatter");
    }
}
