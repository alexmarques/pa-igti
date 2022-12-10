package br.com.pa.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate dataEmissao;
    private String texto;
    @ManyToOne
    private Paciente paciente;
}
