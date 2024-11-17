package org.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class Indexer {
    private final RoAnalyzer analyzer;
    static final String path = "index";
    static final String search_word = "content";
    public Indexer() {
        this.analyzer = new RoAnalyzer();
    }

    public RoAnalyzer getAnalyzer() {
        return this.analyzer;
    }

    public void createIndexFromFiles(String directoryPath) throws IOException, TikaException {
        try {
            Directory indexDirectory = FSDirectory.open(Paths.get(path));

            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            IndexWriter writer = new IndexWriter(indexDirectory, config);

            File dir = new File(directoryPath);
            if (!dir.exists() || !dir.isDirectory()) {
                System.out.println("Directorul specificat nu există sau nu este un director valid.");
                return;
            }

            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        addDocFromFile(writer, file);
                    }
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Eroare la indexarea directorului: " + e.getMessage());
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void addDocFromFile(IndexWriter writer, File file) throws IOException, TikaException, SAXException {
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        try (FileInputStream inputStream = new FileInputStream(file)) {
            parser.parse(inputStream, handler, metadata, context);
        }

        Document doc = new Document();
        doc.add(new TextField(search_word, handler.toString(), Field.Store.YES));
        doc.add(new TextField("Title", file.getName(), Field.Store.YES));
//        doc.add(new TextField("Path", file.getAbsolutePath(), Field.Store.YES));

        writer.addDocument(doc);
//        System.out.println("Indexat fisierul: " + file.getName());
    }
}
