package br.com.pa.controllers;

import br.com.pa.model.Consulta;
import br.com.pa.repository.ConsultaRepository;
import br.com.pa.repository.PacientesRepository;
import br.com.pa.security.UsuarioSistema;
import br.com.pa.services.LuceneIndexerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaRepository consultaRepository;
    private final PacientesRepository pacientesRepository;
    private final LuceneIndexerService indexerService;

    @GetMapping
    public String consultas(Model model,
                            @RequestParam(name = "pacienteId", required = false) Optional<Long> pacienteId,
                            @PageableDefault() Pageable pageable) {
        Page<Consulta> consultas;
        if(pacienteId.isPresent()) {
            consultas = this.consultaRepository.findAllByPacienteId(pacienteId.get(), pageable);
            model.addAttribute("pacienteId", pacienteId.get());
        } else {
            consultas = this.consultaRepository.findAll(pageable);
        }
        model.addAttribute("consultas", consultas);
        model.addAttribute("pacientes", this.pacientesRepository.findAll());
        return "consultas/lista";
    }

    @GetMapping("/novo")
    public String novaConsulta(Model model, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
        model.addAttribute("pacientes", this.pacientesRepository.findAll());
        model.addAttribute("consulta", new Consulta());
        return "consultas/novo";
    }

    @PostMapping("/novo")
    public String salvar(Consulta consulta) throws IOException {
        consulta.setCreatedAt(LocalDateTime.now());
        long documentId = this.indexerService.write(consulta.getTexto());
        consulta.setDocumentId(documentId);
        this.consultaRepository.save(consulta);
        return "redirect:/consultas";
    }

    @GetMapping("/{codigo}/excluir")
    public String excluir(@PathVariable Long codigo, Model model) {
        this.consultaRepository.deleteById(codigo);
        return "redirect:/consultas";
    }
}
