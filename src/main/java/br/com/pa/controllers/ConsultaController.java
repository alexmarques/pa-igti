package br.com.pa.controllers;

import br.com.pa.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaRepository consultaRepository;

    @GetMapping("/consulta/nova")
    public String novaConsulta(Model model) {
        model.addAttribute("consultas", this.consultaRepository.findAll());
        return "consulta";
    }
}
