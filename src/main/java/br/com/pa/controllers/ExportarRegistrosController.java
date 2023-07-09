package br.com.pa.controllers;

import br.com.pa.model.Consulta;
import br.com.pa.model.Paciente;
import br.com.pa.repository.ConsultaRepository;
import br.com.pa.repository.PacientesRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/exportar")
@RequiredArgsConstructor
public class ExportarRegistrosController {

    private final PacientesRepository pacientesRepository;
    private final ConsultaRepository consultaRepository;

    @GetMapping
    public String exportar(Model model) {
        List<Paciente> pacientes = this.pacientesRepository.findAll();
        model.addAttribute("pacientes", pacientes);
        return "exportarRegistros";
    }

    @PostMapping
    public @ResponseBody byte[] exportar(Optional<Long> pacienteId, HttpServletResponse response) {
        if(pacienteId.isPresent()) {
            List<Consulta> consultas = this.consultaRepository.findAllByPacienteId(pacienteId.get());
            return exportarArquivo(consultas, response);
        } else {
            List<Consulta> consultas = this.consultaRepository.findAll();
            return exportarArquivo(consultas, response);
        }
    }

    private byte[] exportarArquivo(List<Consulta> consultas, HttpServletResponse response) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        consultas.stream().map(consulta -> {
            StringBuilder builder = new StringBuilder();
            builder.append("Nome do Paciente: " + consulta.getPaciente().getNome()).append("\n");
            builder.append("Data da Consulta: " + consulta.getCreatedAtFormatted()).append("\n");
            builder.append("Texto: " + consulta.getTexto()).append("\n");
            builder.append("#######################").append("\n");
            return builder.toString();
        }).forEach(consulta -> {
            try {
                outputStream.write(consulta.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=consultas.txt");
        return outputStream.toByteArray();
    }
}
