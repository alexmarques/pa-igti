package br.com.pa.model;

import br.com.pa.PaApplication;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String email;
    private Sexo sexo;
    private LocalDate dataNascimento;
    private String observacoes;
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private Set<Consulta> consultas;

    public String getDataNascimentoFormatted() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataNascimento);
    }

    public static void main(String[] args) {
        Faker faker = Faker.instance(Locale.forLanguageTag("pt-BR"));
        Name name;
        do {
            name = faker.name();
            System.out.println(name.firstName());
            System.out.println(name.fullName());
        } while (!name.fullName().equals("Meire Sales"));
        boolean maleFirstName = PaApplication.isMaleFirstName(name.firstName());
        System.out.println(maleFirstName);
    }
}