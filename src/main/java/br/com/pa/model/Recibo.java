package br.com.pa.model;

import br.com.pa.utils.DateUtils;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Recibo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String texto;
    @ManyToOne
    private Paciente paciente;
    private LocalDateTime createdAt;

    public String getCreatedAtFormatted() {
        return DateUtils.format(this.createdAt);
    }
}
