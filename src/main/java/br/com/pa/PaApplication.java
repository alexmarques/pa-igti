package br.com.pa;

import br.com.pa.model.Consulta;
import br.com.pa.model.Paciente;
import br.com.pa.model.Sexo;
import br.com.pa.model.Usuario;
import br.com.pa.repository.ConsultaRepository;
import br.com.pa.repository.PacientesRepository;
import br.com.pa.repository.UsuarioRepository;
import br.com.pa.services.ConsultaService;
import br.com.pa.services.LuceneIndexerService;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class PaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(UsuarioRepository usuarioRepository,
                                    PacientesRepository pacientesRepository,
                                    ConsultaService consultaService,
                                    PasswordEncoder passwordEncoder,
                                    LuceneIndexerService indexerService) {
        return args -> {

            indexerService.clearIndexDirectory();

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

                    String texto = faker.lorem().fixedString(100);

                    Consulta consulta = Consulta.builder()
                            .texto(texto)
                            .createdAt(LocalDateTime.ofInstant(dataConsulta.toInstant(), ZoneId.systemDefault()))
                            .paciente(pacienteSalvo)
                            .build();

                    consultaService.salvar(consulta);
                }
            }
        };
    }

    public static boolean isMaleFirstName(String name) {
        return stream(maleFirstName.trim().split(","))
                .filter(n -> n.trim().equals(name.trim()))
                .count() > 0;
    }

    public static final String maleFirstName = "Alessandro,Alexandre,Ant??nio,Arthur,Ben??cio,Benjamin,Bernardo,Breno,Bryan" +
            ",Caio,Carlos,Cau??,C??sar,Daniel,Danilo,Davi,Deneval,Djalma,Eduardo,El??sio,Emanuel,Enrico,Enzo,Fabiano,F??bio" +
            ",Fabr??cio,Feliciano,Felipe,F??lix,Francisco,Frederico,Gabriel,G??bio,Guilherme,Gustavo,Heitor,H??lio,Henrique" +
            ",Hugo,??gor,Isaac,Jo??o,Joaquim,J??lio,Kl??ber,Ladislau,Leonardo,Lorenzo,Lucas,Lucca,Marcelo,M??rcio,Marcos,Matheus" +
            ",Miguel,Murilo,Nataniel,Nicolas,Norberto,Pablo,Paulo,Pedro,Pietro,Rafael,Raul,Ricardo,Roberto,Salvador,Samuel" +
            ",Silas,Sirineu,Tertuliano,Theo,Thiago,Thomas,Vicente,V??ctor,Vinicius,Vitor,Warley,Washington,Yago,Yango,Yuri" +
            ",Andr??,Anthony,Arthur Gabriel,Arthur Henrique,Arthur Miguel,Augusto,Bento,Bruno,Calebe,Carlos Eduardo,Davi Lucas" +
            ",Davi Luca,Davi Lucca,Davi Ludmer,Davi Luiz,Davi Miguel,Enzo Gabriel,Enzo Thiago,Enzo Miguel,Erick,Fernando,Gael" +
            ",Henry,Ian,Igor,Jo??o,Jo??o Gabriel,Jo??o Guilherme,Jo??o Lucas,Jo??o Luca,Jo??o Miguel,Jo??o Samuel,Jo??o Pedro,Jo??o Felipe" +
            ",Jo??o Vitor,Jo??o Victor,Jos??,Kaique,Kau??,Levi,Luan,Lucas Gabriel,Luiz,Luiz Felipe,Luiz Gustavo,Luiz Henrique" +
            ",Luiz Miguel,Luiz Ot??vio,Mathias,Nathan,Noah,Oliver,Ot??vio,Pedro Tobias,Pedro Henrique,Pedro Lucas,Pedro Miguel" +
            ",Rodrigo,Ruan,Ryan,Tiago,Tom??s,Vin??cius,Vitor Hugo";
}