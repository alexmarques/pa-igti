package br.com.pa.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String texto;
    private LocalDateTime createdAt;
    @ManyToOne
    private Paciente paciente;
}
