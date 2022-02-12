package br.com.pa.services;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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

    private static final Path indexPath = Path.of("C:\\Users\\Alex\\lucene_index_dir");
    private static final Analyzer analyzer = new PortugueseAnalyzer();

    public static void main(String[] args) throws IOException, ParseException {
        LuceneIndexerService indexerService = new LuceneIndexerService();
        indexerService.write("eu gosto de pizza");
        List<Document> documents = indexerService.read("pizza");
        documents.forEach(System.out::println);
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
        Directory directory = FSDirectory.open(indexPath);
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(directoryReader);
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse(term);
        TopDocs docs = searcher.search(query, 50);
        List<Document> contents = Arrays.stream(docs.scoreDocs)
//                .filter(doc -> doc.score > 0.9)
                .map(doc -> this.readDoc(doc.doc, directoryReader))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        directory.close();
        directoryReader.close();
        return contents;
    }

    private String highlight(Highlighter highlighter, List<IndexableField> field) {
        try {
            return highlighter.getBestFragment(analyzer, "content", field.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return "";
    }

    public long write(String text) throws IOException {
        FSDirectory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        Document document = new Document();
        document.add(new TextField("content", text, Field.Store.YES));
        IndexWriter writer = new IndexWriter(directory, config);
        long id = writer.addDocument(document);
        writer.close();
        directory.close();
        return id;
    }
}
