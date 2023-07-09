package br.com.pa.model;

import br.com.pa.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String texto;
    private LocalDateTime createdAt;
    @ManyToOne
    private Paciente paciente;

    public String getCreatedAtFormatted() {
        return DateUtils.format(this.createdAt);
    }
}
