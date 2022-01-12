package br.com.pa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsultaController {

    @GetMapping("/consulta/nova")
    public String novaConsulta() {
        return "consulta";
    }
}
