package br.com.pa.controllers;

import br.com.pa.dtos.ResultadoBuscaPorTermo;
import br.com.pa.model.Paciente;
import br.com.pa.repository.PacientesRepository;
import br.com.pa.services.ConsultaService;
import br.com.pa.services.LuceneIndexerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/busca")
@RequiredArgsConstructor
public class BuscaController {

    private final PacientesRepository pacientesRepository;
    private final LuceneIndexerService indexerService;
    private final ConsultaService consultaService;

    @GetMapping
    public String busca(Model model) {
        model.addAttribute("pacientes", this.pacientesRepository.findAll());
        return "busca";
    }

    @PostMapping
    public String buscar(Optional<Long> pacienteId, String termo, Model model) throws IOException, ParseException {
        model.addAttribute("pacientes", this.pacientesRepository.findAll());
        List<Document> documentos = this.indexerService.read(termo);
        Query query = this.indexerService.getQueryForTerm(termo);
        QueryScorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(scorer);
        if(pacienteId.isPresent()) {
            Paciente paciente = this.pacientesRepository.findById(pacienteId.get()).get();
            List<ResultadoBuscaPorTermo> resultados = documentos.stream()
                    .filter(document -> this.isSamePaciente(document, pacienteId.get()))
                    .map(document -> {
                        IndexableField consultaId = document.getField(LuceneIndexerService.CONSULTA_ID_FIELD_NAME);
                        IndexableField texto = document.getField(LuceneIndexerService.TEXTO_FIELD_NAME);
                        return new ImmutablePair(consultaId, texto);
                    })
                    .map(pair -> {
                        IndexableField texto = IndexableField.class.cast(pair.getRight());
                        String highlight = this.indexerService.highlight(highlighter, texto);
                        long consultaId = IndexableField.class.cast(pair.getLeft()).numericValue().longValue();
                        return new ResultadoBuscaPorTermo(paciente.getNome(), consultaId, highlight);
                    }).collect(Collectors.toList());
            model.addAttribute("resultados", resultados);
            return "busca";
        } else {

        }
        // vou precisar nome do paciente, id da consulta e o texto destacado
        return "busca";
    }

    private boolean isSamePaciente(Document document, Long pacienteId) {
        IndexableField field = document.getField(LuceneIndexerService.PACIENTE_ID_FIELD_NAME);
        long fieldLongValue = field.numericValue().longValue();
        return fieldLongValue == pacienteId.longValue();
    }
}
