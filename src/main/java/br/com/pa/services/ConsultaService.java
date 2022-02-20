package br.com.pa.services;

import br.com.pa.dtos.ConsultaId;
import br.com.pa.dtos.PacienteId;
import br.com.pa.exceptions.ConsultaNotFoundException;
import br.com.pa.model.Consulta;
import br.com.pa.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final LuceneIndexerService indexerService;

    public void salvar(Consulta consulta) {
        if(Objects.isNull(consulta.getCreatedAt())) {
            consulta.setCreatedAt(LocalDateTime.now());
        }
        Consulta consultaSalva = this.consultaRepository.save(consulta);
        try {
            var consultaId = new ConsultaId(consultaSalva.getId());
            var pacienteId = new PacienteId(consultaSalva.getPaciente().getId());
            this.indexerService.write(consultaId, pacienteId, consulta.getPaciente().getNome(), consultaSalva.getTexto());
        } catch (IOException e) {
            log.error("Falha ao salvar consulta no indexador", e);
        }
    }

    public Page<Consulta> findAllByPacienteId(Long pacienteId, Pageable pageable) {
        PageRequest sort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        return this.consultaRepository.findAllByPacienteId(pacienteId, sort);
    }

    public List<Consulta> findAllByPacienteId(Long pacienteId) {
        return this.consultaRepository.findAllByPacienteId(pacienteId);
    }

    public Page<Consulta> findAll(Pageable pageable) {
        PageRequest sort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        return this.consultaRepository.findAll(sort);
    }

    public void deleteById(Long id) {
        this.consultaRepository.deleteById(id);
    }

    public Consulta findById(Long id) {
        return this.consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNotFoundException("Consulta " + id + " não encontrada"));
    }
}
