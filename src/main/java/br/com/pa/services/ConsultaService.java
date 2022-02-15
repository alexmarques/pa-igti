package br.com.pa.services;

import br.com.pa.dtos.ConsultaId;
import br.com.pa.dtos.PacienteId;
import br.com.pa.model.Consulta;
import br.com.pa.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final LuceneIndexerService indexerService;

    public void salvar(Consulta consulta) {
        consulta.setCreatedAt(LocalDateTime.now());
        Consulta consultaSalva = this.consultaRepository.save(consulta);
        try {
            var consultaId = new ConsultaId(consultaSalva.getId());
            var pacienteId = new PacienteId(consultaSalva.getPaciente().getId());
            this.indexerService.write(consultaId, pacienteId, consultaSalva.getTexto());
        } catch (IOException e) {
            log.error("Falha ao salvar consulta no indexador", e);
        }
    }

    public Page<Consulta> findAllByPacienteId(Long pacienteId, Pageable pageable) {
        return this.consultaRepository.findAllByPacienteId(pacienteId, pageable);
    }

    public List<Consulta> findAllByPacienteId(Long pacienteId) {
        return this.consultaRepository.findAllByPacienteId(pacienteId);
    }

    public Page<Consulta> findAll(Pageable pageable) {
        return this.consultaRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        this.consultaRepository.deleteById(id);
    }
}
