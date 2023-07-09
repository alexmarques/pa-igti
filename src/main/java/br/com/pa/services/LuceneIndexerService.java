package br.com.pa.services;

import br.com.pa.dtos.ConsultaId;
import br.com.pa.dtos.PacienteId;
import br.com.pa.lucene.CustomFormatter;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LuceneIndexerService {


    public static final String CONSULTA_ID_FIELD_NAME = "consultaId";
    public static final String PACIENTE_ID_FIELD_NAME = "pacienteId";
    public static final String PACIENTE_NOME_FIELD_NAME = "pacienteNome";
    public static final String TEXTO_FIELD_NAME = "content";
    public static final Formatter CUSTOM_FORMATTER = new CustomFormatter(1.0F,"#000000","#000000","#DFD321","#DFD321");

    private final Path luceneIndexPath;
    private static final Analyzer PORTUGUESE_ANALYZER = new PortugueseAnalyzer();
    private static final QueryParser PARSER = new QueryParser(TEXTO_FIELD_NAME, PORTUGUESE_ANALYZER);

    public LuceneIndexerService(@Value("${lucene.index.dir}") String luceneIndexPath) {
        this.luceneIndexPath = Path.of(luceneIndexPath);
    }

    private Document readDoc(int docId, DirectoryReader directory) {
        try {
            return directory.document(docId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Document> read(String term) throws IOException, ParseException {
        Directory directory = FSDirectory.open(luceneIndexPath);
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(directoryReader);
        Query query = PARSER.parse(term);
        TopDocs docs = searcher.search(query, 50);
        List<Document> contents = Arrays.stream(docs.scoreDocs)
                .map(doc -> this.readDoc(doc.doc, directoryReader))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        directory.close();
        directoryReader.close();
        return contents;
    }

    public Query getQueryForTerm(String term) throws ParseException {
        return PARSER.parse(term);
    }

    public String highlight(Highlighter highlighter, IndexableField field) {
        try {
            return highlighter.getBestFragment(PORTUGUESE_ANALYZER, TEXTO_FIELD_NAME, field.stringValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void write(ConsultaId consultaId, PacienteId pacienteId, String pacienteNome, String text) throws IOException {
        FSDirectory directory = FSDirectory.open(luceneIndexPath);
        IndexWriterConfig config = new IndexWriterConfig(PORTUGUESE_ANALYZER);
        Document document = new Document();
        document.add(new StoredField(CONSULTA_ID_FIELD_NAME, consultaId.id()));
        document.add(new StoredField(PACIENTE_ID_FIELD_NAME, pacienteId.id()));
        document.add(new TextField(TEXTO_FIELD_NAME, text, Field.Store.YES));
        document.add(new StringField(PACIENTE_NOME_FIELD_NAME, pacienteNome, Field.Store.YES));
        IndexWriter writer = new IndexWriter(directory, config);
        writer.addDocument(document);
        writer.close();
        directory.close();
    }

    public void clearIndexDirectory() {
        try {
            FileUtils.cleanDirectory(luceneIndexPath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
