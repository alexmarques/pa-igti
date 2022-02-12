package br.com.pa.controllers;

import br.com.pa.model.Usuario;
import br.com.pa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index() {
        return "redirect:consultas";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/consultas";
        }

        return "login";
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar() {
        ModelAndView modelAndView = new ModelAndView("cadastro");
        modelAndView.addObject("usuario", new Usuario());
        return modelAndView;
    }

    @PostMapping("/cadastrar")
    public String cadastrar(Usuario usuario, RedirectAttributes attributes) {
        usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
        this.usuarioRepository.save(usuario);
        attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
        return "redirect:/login";
    }

    @GetMapping("/403")
    public String acessoNegado() {
        return "403";
    }
}
