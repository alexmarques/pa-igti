package br.com.pa.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private Sexo sexo;
    @OneToMany(mappedBy = "paciente")
    private Set<Consulta> consultas;
}
