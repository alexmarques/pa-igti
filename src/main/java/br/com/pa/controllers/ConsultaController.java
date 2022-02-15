package br.com.pa.controllers;

import br.com.pa.model.Consulta;
import br.com.pa.repository.PacientesRepository;
import br.com.pa.services.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/consultas")
public class ConsultaController {

    private final PacientesRepository pacientesRepository;
    private final ConsultaService consultaService;

    @GetMapping
    public String consultas(Model model,
                            @RequestParam(name = "pacienteId", required = false) Optional<Long> pacienteId,
                            @PageableDefault() Pageable pageable) {
        Page<Consulta> consultas;
        if(pacienteId.isPresent()) {
            consultas = this.consultaService.findAllByPacienteId(pacienteId.get(), pageable);
            model.addAttribute("pacienteId", pacienteId.get());
        } else {
            consultas = this.consultaService.findAll(pageable);
        }
        model.addAttribute("consultas", consultas);
        model.addAttribute("pacientes", this.pacientesRepository.findAll());
        return "consultas/lista";
    }

    @GetMapping("/novo")
    public String novaConsulta(Model model) {
        model.addAttribute("pacientes", this.pacientesRepository.findAll());
        model.addAttribute("consulta", new Consulta());
        return "consultas/novo";
    }

    @PostMapping("/novo")
    public String salvar(Consulta consulta) {
        this.consultaService.salvar(consulta);
        return "redirect:/consultas";
    }

    @GetMapping("/{codigo}/excluir")
    public String excluir(@PathVariable Long codigo) {
        this.consultaService.deleteById(codigo);
        return "redirect:/consultas";
    }
}
