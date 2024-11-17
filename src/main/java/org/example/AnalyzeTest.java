package org.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.TokenStream;
import org.example.RoAnalyzer;

import java.io.IOException;
import java.io.StringReader;

public class AnalyzeTest {
    public static void main(String[] args) {
        Analyzer analyzer = new RoAnalyzer();

        String text = "CĂUTARE este o bibliotecă pentru procesare şi și ai căutare de text cu diacriticele!";

        try {
            String analyzedText = analyzeText(analyzer, text);
            System.out.println("Text analizat: " + analyzedText);
//            System.out.println(RomanianAnalyzer.getDefaultStopSet());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            analyzer.close();
        }
    }

    /**
     * Analizează textul utilizând un Analyzer și returnează termeni analizați ca șir de text.
     * @param analyzer Analyzer-ul utilizat
     * @param text Textul de analizat
     * @return Termeni analizați separați prin spațiu
     * @throws IOException Dacă apare o eroare în procesarea TokenStream-ului
     */
    public static String analyzeText(Analyzer analyzer, String text) throws IOException {
        StringBuilder analyzedTokens = new StringBuilder();
//
        TokenStream tokenStream = analyzer.tokenStream("fieldName", new StringReader(text));

        CharTermAttribute charTermAttr = tokenStream.addAttribute(CharTermAttribute.class);

        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            if (analyzedTokens.length() > 0) {
                analyzedTokens.append(" ");
            }
            analyzedTokens.append(charTermAttr.toString());
        }
        tokenStream.end();
        tokenStream.close();

        return analyzedTokens.toString();
    }
}
