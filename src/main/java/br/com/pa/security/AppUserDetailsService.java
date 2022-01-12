package br.com.pa.security;

import br.com.pa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return this.usuarioRepository.findByEmail(email)
				.map(u -> new User(u.getEmail(), u.getSenha(), Collections.emptyList()))
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}
}
