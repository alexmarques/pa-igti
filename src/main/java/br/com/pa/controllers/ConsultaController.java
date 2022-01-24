package br.com.pa.controllers;

import br.com.pa.model.Consulta;
import br.com.pa.repository.ConsultaRepository;
import br.com.pa.repository.PacientesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaRepository consultaRepository;
    private final PacientesRepository pacientesRepository;

    @GetMapping
    public String consultas(Model model) {
        model.addAttribute("consultas", this.consultaRepository.findAll());
        return "consultas/lista";
    }

    @GetMapping("/novo")
    public String novaConsulta(Model model) {
        model.addAttribute("pacientes", this.pacientesRepository.findAll());
        model.addAttribute("consulta", new Consulta());
        return "consultas/novo";
    }

    @PostMapping("/novo")
    public String salvar(Consulta consulta, Model model) {
        consulta.setCreatedAt(LocalDateTime.now());
        this.consultaRepository.save(consulta);
        return this.consultas(model);
    }

    @GetMapping("/{codigo}/excluir")
    public String excluir(@PathVariable Long codigo, Model model) {
        this.consultaRepository.deleteById(codigo);
        return consultas(model);
    }
}
