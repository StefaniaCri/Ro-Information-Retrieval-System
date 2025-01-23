package org.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.Normalizer;

import static org.example.Indexer.search_word;

public class Searcher {
    private final RoAnalyzer analyzer;
    private final String indexDirectoryPath = Indexer.path;

    public Searcher(RoAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public static String removeDiacriticsManually(String text) {
        String[][] replacements = {
                {"ș", "s"}, {"Ș", "S"},
                {"ț", "t"}, {"Ț", "T"},
        };

        for (String[] pair : replacements) {
            text = text.replace(pair[0], pair[1]);
        }
//        System.out.println(text);
        return text;
    }

    public void search(String querystr) throws Exception {
        querystr = removeDiacriticsManually(querystr);
        String normalizedWord = Normalizer.normalize(querystr, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        Query q = new QueryParser(search_word, analyzer).parse(normalizedWord);

        int hitsPerPage = 5;
        Directory directory = FSDirectory.open(Paths.get(indexDirectoryPath));
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("Title"));
        }

        reader.close();
    }
}
