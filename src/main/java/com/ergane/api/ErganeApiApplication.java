package com.ergane.api;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ergane.api.model.Usuario;
import com.ergane.api.repository.UsuarioRepository;

@SpringBootApplication
public class ErganeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErganeApiApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner inicializarDados(Environment env, UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder encoder) {
        return args -> {
            System.out.println("\n🚀 API do Ergane inicializada com sucesso!");

            if (usuarioRepository.count() == 0) {
                Usuario admin = Usuario.builder()
                        .nome("Vendedor Teste")
                        .cpf("12345678900")
                        .senhaHash(encoder.encode("senha123"))
                        .dataHoraCadastro(LocalDateTime.now())
                        .build();
                usuarioRepository.save(admin);
                System.out.println("✅ Utilizador de teste criado! CPF: 12345678900 | Senha: senha123");
            }
        };
    }

    // Habilita transações no MongoDB para que a baixa de estoque + registro da venda
    // sejam atômicos (exige um cluster em replica set, como o MongoDB Atlas).
    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:http://localhost:8081}") List<String> allowedOrigins) {
        CorsConfiguration configuration = new CorsConfiguration();
        // Origens explícitas em vez de "*". A autenticação é via Bearer token,
        // então não habilitamos credenciais (cookies) — evita wildcard + credentials.
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowCredentials(false);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}