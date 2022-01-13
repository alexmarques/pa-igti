package br.com.pa;

import br.com.pa.model.Usuario;
import br.com.pa.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Usuario usuario = new Usuario();
            usuario.setEmail("email@email.com");
            usuario.setSenha(passwordEncoder.encode("admin"));
            usuario.setConfirmacaoSenha(passwordEncoder.encode("admin"));
            usuario.setNome("Alex");
            usuarioRepository.save(usuario);
        };

    }

}
