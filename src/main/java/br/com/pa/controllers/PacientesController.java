package br.com.pa.controllers;

import br.com.pa.model.Paciente;
import br.com.pa.model.Sexo;
import br.com.pa.repository.PacientesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacientesController {

    private final PacientesRepository pacientesRepository;

    @GetMapping
    public String pacientes(Model model) {
        model.addAttribute("pacientes", this.pacientesRepository.findAll());
        return "pacientes";
    }

    @GetMapping("/novo")
    public String novoPaciente(Model model) {
        model.addAttribute("sexos", Sexo.values());
        model.addAttribute("paciente", new Paciente());
        return "novoPaciente";
    }

    @PostMapping("/novo")
    public String cadastrarPaciente(Paciente paciente, RedirectAttributes attributes) {
        this.pacientesRepository.save(paciente);
        attributes.addFlashAttribute("clienteCadastrado", "Cliente cadastrado com sucesso!");
        return "redirect:/pacientes";
    }
}
