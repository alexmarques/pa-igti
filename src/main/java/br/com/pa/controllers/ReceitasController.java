package br.com.pa.controllers;

import br.com.pa.model.Receita;
import br.com.pa.repository.PacientesRepository;
import br.com.pa.repository.ReceitasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/receitas")
@RequiredArgsConstructor
public class ReceitasController {

    private final ReceitasRepository receitasRepository;
    private final PacientesRepository pacientesRepository;

    @GetMapping
    public String receitas(Model model) {
        model.addAttribute("receitas", this.receitasRepository.findAll());
        return "receitas/lista";
    }

    @GetMapping("/novo")
    public String nova(Model model) {
        model.addAttribute("pacientes", this.pacientesRepository.findAll());
        model.addAttribute("receita", new Receita());
        return "receitas/novo";
    }

    @PostMapping("/novo")
    public String salvarReceita(Receita receita, RedirectAttributes attributes) {
        receita.setDataEmissao(LocalDate.now());
        this.receitasRepository.save(receita);
        attributes.addFlashAttribute("mensagem", "Receita emitida com sucesso!");
        return "redirect:/receitas";

    }
}
