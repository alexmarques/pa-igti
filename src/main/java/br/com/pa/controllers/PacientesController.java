package br.com.pa.controllers;

import br.com.pa.model.Paciente;
import br.com.pa.model.Sexo;
import br.com.pa.repository.PacientesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacientesController {

    private final PacientesRepository pacientesRepository;

    @GetMapping
    public ModelAndView pacientes() {
        ModelAndView mv = new ModelAndView("pacientes/lista");
        mv.addObject("pacientes", this.pacientesRepository.findAll());
        return mv;
    }

    @GetMapping("/{codigo}")
    public ModelAndView editar(@PathVariable Long codigo, Model model) {
        ModelAndView mv = new ModelAndView("pacientes/editar");
        model.addAttribute("sexos", Sexo.values());
        mv.addObject("paciente", this.pacientesRepository.findById(codigo).get());
        return mv;
    }

    @GetMapping("/novo")
    public String novoPaciente(Model model) {
        model.addAttribute("sexos", Sexo.values());
        model.addAttribute("paciente", new Paciente());
        return "pacientes/novo";
    }

    @PostMapping("/novo")
    public String cadastrarPaciente(Paciente paciente, RedirectAttributes attributes) {
        this.pacientesRepository.save(paciente);
        attributes.addFlashAttribute("pacienteMsg", "Cliente cadastrado com sucesso!");
        return "redirect:/pacientes";
    }

    @PutMapping
    public ModelAndView alterarPaciente(Paciente paciente,
                                        RedirectAttributes attributes) {
        attributes.addAttribute("pacienteMsg", "Cliente alterado com sucesso!");
        this.pacientesRepository.save(paciente);
        return pacientes();
    }

    @DeleteMapping
    public ModelAndView deletarCliente(HttpServletRequest request) {
        Long codigo = Long.valueOf(request.getParameter("codigo"));
        this.pacientesRepository.deleteById(codigo);
        return pacientes();
    }
}
