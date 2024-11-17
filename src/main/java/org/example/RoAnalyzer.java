package org.example;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.tartarus.snowball.ext.RomanianStemmer;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public class RoAnalyzer extends Analyzer {

    private static CharArraySet removeDiacriticsFromStopSet(CharArraySet stopSet) {
        CharArraySet result = new CharArraySet(stopSet.size(), true);
        for (Object word : stopSet) {
            String stopWord;
            if (word instanceof char[]) {
                stopWord = new String((char[]) word);
            } else if (word instanceof String) {
                stopWord = (String) word;
            } else {
                continue;
            }

            String normalizedWord = Normalizer.normalize(stopWord, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", ""); // Elimină marcajele diacritice

            result.add(normalizedWord);
        }
        return result;
    }
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new StandardTokenizer();

        TokenStream filter = new LowerCaseFilter(tokenizer);
        CharArraySet stopSet = CharArraySet.copy(RomanianAnalyzer.getDefaultStopSet());
        stopSet.add("și");
        stopSet.add("niște");
        stopSet.add("acești");
        stopSet.add("aș");
        stopSet.add("deși");
        stopSet.add("noștri");
        stopSet.add("totuși");
        stopSet.add("ești");
        stopSet.add("voștri");
        stopSet.add("așadar");
        stopSet.add("aceștia");
        stopSet.add("ăștia");
        stopSet.add("mulți");
        stopSet.add("aveți");
        stopSet.add("sînteți");
        stopSet.add("toți");
        stopSet.add("îți");
        stopSet.add("ți");
        stopSet.add("fiți");
        stopSet.add("câți");
        stopSet.add("ați");
        stopSet.add("ție");
        stopSet.add("cîți");
        stopSet.add("sunteți");
        filter = new StopFilter(filter, stopSet);


        filter = new SnowballFilter(filter, new RomanianStemmer());

        filter = new RemoveDiacriticsFilter(filter);

//        CharArraySet defaultStopSet = RomanianAnalyzer.getDefaultStopSet();
//
//        CharArraySet stopWordsWithoutDiacritics = removeDiacriticsFromStopSet(defaultStopSet);
//        filter = new StopFilter(filter, stopWordsWithoutDiacritics);

        return new TokenStreamComponents(tokenizer, filter);
    }
}

class RemoveDiacriticsFilter extends TokenFilter {
    private final CharTermAttribute charTermAttr = addAttribute(CharTermAttribute.class);

    protected RemoveDiacriticsFilter(TokenStream input) {
        super(input);
    }

    public static String removeDiacriticsManually(String text) {
        String[][] replacements = {
                {"ș", "s"}, {"Ș", "S"},
                {"ț", "t"}, {"Ț", "T"},
        };

        for (String[] pair : replacements) {
            text = text.replace(pair[0], pair[1]);
        }

        return text;
    }
    @Override
    public boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            String term = charTermAttr.toString();
            term = removeDiacriticsManually(term);
            String normalized = Normalizer.normalize(term, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "");
            charTermAttr.setEmpty().append(normalized);
            return true;
        }
        return false;
    }
}