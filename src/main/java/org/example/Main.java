package org.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        boolean isIndex = false;
        boolean isSearch = false;
        String directory = null;
        String query = null;
        Indexer indexer = new Indexer();
        Searcher searcher = new Searcher(indexer.getAnalyzer());


        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-index":
                    isIndex = true;
                    break;
                case "-directory":
                    if (i + 1 < args.length) {
                        directory = args[++i];
                    } else {
                        System.out.println("Error: -directory requires a value");
                        return;
                    }
                    break;
                case "-search":
                    isSearch = true;
                    break;
                case "-query":
                    if (i + 1 < args.length) {
                        query = args[++i];
                    } else {
                        System.out.println("Error: -query requires a value");
                        return;
                    }
                    break;
                default:
                    System.out.println("Unknown option: " + args[i]);
                    return;
            }
        }

        if (isIndex) {
            if (directory == null) {
                System.out.println("Error: -index requires -directory <path>");
                return;
            }
            indexer.createIndexFromFiles(directory);

        } else if (isSearch) {
            if (query == null) {
                System.out.println("Error: -search requires -query <keyword>");
                return;
            }

            searcher.search(query);

        } else {
            System.out.println("Error: Please specify either -index or -search.");
        }
    }
}

