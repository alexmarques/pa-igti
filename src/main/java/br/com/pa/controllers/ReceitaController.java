package br.com.pa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReceitaController {
    @GetMapping("/receita")
    public String receita() {
        return "receita";
    }
}