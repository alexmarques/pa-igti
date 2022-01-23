package br.com.pa.model;

import lombok.Data;

import javax.persistence.*;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@Data
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String email;
    private Sexo sexo;
    private LocalDate dataNascimento;
    private String observacoes;
    @OneToMany(mappedBy = "paciente")
    private Set<Consulta> consultas;

    public String getDataNascimentoFormatted() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataNascimento);
    }
}
