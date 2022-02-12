package br.com.pa;

import br.com.pa.model.Consulta;
import br.com.pa.model.Paciente;
import br.com.pa.model.Sexo;
import br.com.pa.model.Usuario;
import br.com.pa.repository.ConsultaRepository;
import br.com.pa.repository.PacientesRepository;
import br.com.pa.repository.UsuarioRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.val;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unbescape.css.CssStringEscapeLevel;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.*;

@SpringBootApplication
@EnableFeignClients
@EnableSpringDataWebSupport
public class PaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(UsuarioRepository usuarioRepository,
                                    PacientesRepository pacientesRepository,
                                    ConsultaRepository consultaRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {

            Usuario usuario = new Usuario();
            usuario.setEmail("email@email.com");
            usuario.setSenha(passwordEncoder.encode("admin"));
            usuario.setConfirmacaoSenha(passwordEncoder.encode("admin"));
            usuario.setNome("Alex");
            usuarioRepository.save(usuario);

            Faker faker = Faker.instance(Locale.forLanguageTag("pt-BR"));

            for(int x = 0; x <= 10; x++) {

                Date dataDeNascimento = faker.date().birthday(18, 55);

                String name = faker.name().firstName();

                Paciente paciente = Paciente.builder()
                        .nome(name)
                        .email(faker.bothify("????###@email.com"))
                        .sexo(isMaleFirstName(name) ? Sexo.MASCULINO : Sexo.FEMININO)
                        .dataNascimento(LocalDate.ofInstant(dataDeNascimento.toInstant(), ZoneId.systemDefault()))
                        .build();

                Paciente pacienteSalvo = pacientesRepository.save(paciente);

                for(int y = 0; y <= 20; y++) {

                    Date dataConsulta = faker.date().past(5 * 30, TimeUnit.DAYS);

                    Consulta consulta = Consulta.builder()
                            .texto(faker.lorem().characters(100))
                            .createdAt(LocalDateTime.ofInstant(dataConsulta.toInstant(), ZoneId.systemDefault()))
                            .paciente(pacienteSalvo)
                            .build();

                    consultaRepository.save(consulta);
                }
            }
        };
    }

    public static boolean isMaleFirstName(String name) {
        return stream(maleFirstName.trim().split(","))
                .filter(n -> n.trim().equals(name.trim()))
                .count() > 0;
    }

    public static final String maleFirstName = "Alessandro,Alexandre,Antônio,Arthur,Benício,Benjamin,Bernardo,Breno,Bryan" +
            ",Caio,Carlos,Cauã,César,Daniel,Danilo,Davi,Deneval,Djalma,Eduardo,Elísio,Emanuel,Enrico,Enzo,Fabiano,Fábio" +
            ",Fabrício,Feliciano,Felipe,Félix,Francisco,Frederico,Gabriel,Gúbio,Guilherme,Gustavo,Heitor,Hélio,Henrique" +
            ",Hugo,Ígor,Isaac,João,Joaquim,Júlio,Kléber,Ladislau,Leonardo,Lorenzo,Lucas,Lucca,Marcelo,Márcio,Marcos,Matheus" +
            ",Miguel,Murilo,Nataniel,Nicolas,Norberto,Pablo,Paulo,Pedro,Pietro,Rafael,Raul,Ricardo,Roberto,Salvador,Samuel" +
            ",Silas,Sirineu,Tertuliano,Theo,Thiago,Thomas,Vicente,Víctor,Vinicius,Vitor,Warley,Washington,Yago,Yango,Yuri" +
            ",André,Anthony,Arthur Gabriel,Arthur Henrique,Arthur Miguel,Augusto,Bento,Bruno,Calebe,Carlos Eduardo,Davi Lucas" +
            ",Davi Luca,Davi Lucca,Davi Ludmer,Davi Luiz,Davi Miguel,Enzo Gabriel,Enzo Thiago,Enzo Miguel,Erick,Fernando,Gael" +
            ",Henry,Ian,Igor,João,João Gabriel,João Guilherme,João Lucas,João Luca,João Miguel,João Samuel,João Pedro,João Felipe" +
            ",João Vitor,João Victor,José,Kaique,Kauê,Levi,Luan,Lucas Gabriel,Luiz,Luiz Felipe,Luiz Gustavo,Luiz Henrique" +
            ",Luiz Miguel,Luiz Otávio,Mathias,Nathan,Noah,Oliver,Otávio,Pedro Tobias,Pedro Henrique,Pedro Lucas,Pedro Miguel" +
            ",Rodrigo,Ruan,Ryan,Tiago,Tomás,Vinícius,Vitor Hugo";
}