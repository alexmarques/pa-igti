package br.com.pa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClienteController {

    @GetMapping("/cliente/novo")
    public String novoCliente() {
        return "novoCliente";
    }
}
