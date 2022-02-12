package br.com.pa.repository;

import br.com.pa.model.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    Page<Consulta> findAllByPacienteId(Long pacienteId, Pageable pageable);
    List<Consulta> findAllByPacienteId(Long pacienteId);
}
