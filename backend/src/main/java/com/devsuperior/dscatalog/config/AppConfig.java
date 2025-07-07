package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
// Classe de configuração
public class AppConfig {

    // O @Bean é um componente do spring, e colocando esta anotação nós informamos que este metodo
    // será gerenciado pelo spring e poderemos injetá-lo em outras classes
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
