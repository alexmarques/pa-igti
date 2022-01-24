package br.com.pa;

import br.com.pa.model.Paciente;
import br.com.pa.model.Sexo;
import br.com.pa.model.Usuario;
import br.com.pa.repository.PacientesRepository;
import br.com.pa.repository.UsuarioRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@SpringBootApplication
public class PaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(UsuarioRepository usuarioRepository, PacientesRepository pacientesRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Usuario usuario = new Usuario();
            usuario.setEmail("email@email.com");
            usuario.setSenha(passwordEncoder.encode("admin"));
            usuario.setConfirmacaoSenha(passwordEncoder.encode("admin"));
            usuario.setNome("Alex");
            usuarioRepository.save(usuario);

            Paciente paciente = new Paciente();
            paciente.setNome("Alex");
            paciente.setEmail("alex@email.com");
            paciente.setSexo(Sexo.MASCULINO);
            paciente.setDataNascimento(LocalDate.of(1983, 7, 27));
            pacientesRepository.save(paciente);
        };

    }

}