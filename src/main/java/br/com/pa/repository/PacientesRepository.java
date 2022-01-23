package br.com.pa.repository;

import br.com.pa.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacientesRepository extends JpaRepository<Paciente, Long> {
}
