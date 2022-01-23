package br.com.pa.controllers;

import br.com.pa.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("consultas")
public class ConsultaController {

    private final ConsultaRepository consultaRepository;

    @GetMapping
    public String consultas(Model model) {
        model.addAttribute("consultas", this.consultaRepository.findAll());
        return "consulta";
    }

    @GetMapping("nova")
    public String novaConsulta(Model model) {
        model.addAttribute("consultas", this.consultaRepository.findAll());
        return "consulta";
    }
}
