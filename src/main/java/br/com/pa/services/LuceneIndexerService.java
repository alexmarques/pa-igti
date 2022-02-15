package br.com.pa.services;

import br.com.pa.dtos.ConsultaId;
import br.com.pa.dtos.PacienteId;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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
    public static final String TEXTO_FIELD_NAME = "content";

    private static final Path INDEX_PATH = Path.of("C:\\Users\\Alex\\lucene_index_dir");
    private static final Analyzer PORTUGUESE_ANALYZER = new PortugueseAnalyzer();
    private static final QueryParser PARSER = new QueryParser(TEXTO_FIELD_NAME, PORTUGUESE_ANALYZER);

    private Document readDoc(int docId, DirectoryReader directory) {
        try {
            return directory.document(docId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Document> read(String term) throws IOException, ParseException {
        Directory directory = FSDirectory.open(INDEX_PATH);
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

    public void write(ConsultaId consultaId, PacienteId pacienteId, String text) throws IOException {
        FSDirectory directory = FSDirectory.open(INDEX_PATH);
        IndexWriterConfig config = new IndexWriterConfig(PORTUGUESE_ANALYZER);
        Document document = new Document();
        document.add(new StoredField(CONSULTA_ID_FIELD_NAME, consultaId.getId()));
        document.add(new StoredField(PACIENTE_ID_FIELD_NAME, pacienteId.getId()));
        document.add(new TextField(TEXTO_FIELD_NAME, text, Field.Store.YES));
        IndexWriter writer = new IndexWriter(directory, config);
        writer.addDocument(document);
        writer.close();
        directory.close();
    }
}
