package com.ergane.api;

import com.ergane.api.model.Usuario;
import com.ergane.api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class ErganeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErganeApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner inicializarDados(Environment env, UsuarioRepository usuarioRepository) {
		return args -> {
			System.out.println("\n🚀 API do Ergane inicializada com sucesso!");

			// Cria um utilizador de teste caso não exista nenhum
			if (usuarioRepository.count() == 0) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
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
}