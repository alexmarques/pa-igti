package br.com.pa.controllers;

import br.com.pa.model.Recibo;
import br.com.pa.repository.RecibosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/recibos")
@RequiredArgsConstructor
public class RecibosController {

    private final RecibosRepository recibosRepository;

    @GetMapping
    public String index(Model model) {
        List<Recibo> recibos = this.recibosRepository.findAll();
        model.addAttribute("recibos", recibos);
        return "recibos/lista";
    }

    @GetMapping("/novo")
    public String novo() {
        return "recibos/novo";
    }
}
