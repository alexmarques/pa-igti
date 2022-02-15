package br.com.pa.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoBuscaPorTermo {
    private String nomePaciente;
    private Long consultaId;
    private String texto;
}
