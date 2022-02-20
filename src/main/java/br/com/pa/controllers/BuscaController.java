package br.com.pa.controllers;

import br.com.pa.dtos.ResultadoBuscaPorTermo;
import br.com.pa.lucene.CustomFormatter;
import br.com.pa.model.Paciente;
import br.com.pa.repository.PacientesRepository;
import br.com.pa.services.LuceneIndexerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SpanGradientFormatter;
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
        Highlighter highlighter = new Highlighter(LuceneIndexerService.CUSTOM_FORMATTER, scorer);
        if(pacienteId.isPresent()) {
            List<ResultadoBuscaPorTermo> resultados = documentos.stream()
                    .filter(document -> this.isSamePaciente(document, pacienteId.get()))
                    .map(this::getTriple)
                    .map(triple -> getResultadoBuscaPorTermo(highlighter, triple))
                    .collect(Collectors.toList());
            model.addAttribute("resultados", resultados);
            return "busca";
        } else {
            List<ResultadoBuscaPorTermo> resultados = documentos.stream()
                    .map(this::getTriple)
                    .map(triple -> getResultadoBuscaPorTermo(highlighter, triple))
                    .collect(Collectors.toList());
            model.addAttribute("resultados", resultados);
        }
        return "busca";
    }

    private ResultadoBuscaPorTermo getResultadoBuscaPorTermo(Highlighter highlighter, ImmutableTriple<IndexableField, IndexableField, IndexableField> triple) {
        IndexableField texto = IndexableField.class.cast(triple.getMiddle());
        String highlight = this.indexerService.highlight(highlighter, texto);
        long consultaId = IndexableField.class.cast(triple.getLeft()).numericValue().longValue();
        String pacienteNome = triple.getRight().stringValue();
        return new ResultadoBuscaPorTermo(pacienteNome, consultaId, highlight);
    }

    private ImmutableTriple<IndexableField, IndexableField, IndexableField> getTriple(Document document) {
        IndexableField consultaId = document.getField(LuceneIndexerService.CONSULTA_ID_FIELD_NAME);
        IndexableField texto = document.getField(LuceneIndexerService.TEXTO_FIELD_NAME);
        IndexableField pacienteNome = document.getField(LuceneIndexerService.PACIENTE_NOME_FIELD_NAME);
        return new ImmutableTriple<>(consultaId, texto, pacienteNome);
    }

    private boolean isSamePaciente(Document document, Long pacienteId) {
        IndexableField field = document.getField(LuceneIndexerService.PACIENTE_ID_FIELD_NAME);
        long fieldLongValue = field.numericValue().longValue();
        return fieldLongValue == pacienteId.longValue();
    }
}
