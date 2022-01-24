package br.com.pa.repository;

import br.com.pa.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitasRepository extends JpaRepository<Receita, Long> {
}
