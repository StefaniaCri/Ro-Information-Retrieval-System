# Ro-Information-Retrieval-System

Ro-Information-Retrieval-System is a project designed for indexing and searching information in text documents using advanced libraries such as Apache Lucene and Apache Tika. 
This system is optimized for the Romanian language, including functionalities like diacritic processing, stopword removal, and stemming.

---

## Features

1. **Document Indexing**  
   - Uses Apache Tika to extract text content from files.  
   - Creates a Lucene index for efficient searching.

2. **Information Search**  
   - Allows fast and precise searches using a custom analysis system (RoAnalyzer).  
   - Supports diacritic-insensitive searches.

3. **Advanced Text Processing**  
   - Diacritic removal.  
   - Removal of Romanian-specific stopwords.  
   - Stemming using the Snowball algorithm for the Romanian language.



## Project Structure

- `src/main/java/org/example`  
  Contains the main classes:
  - **RoAnalyzer.java**: Custom analyzer for the Romanian language.  
  - **Indexer.java**: Responsible for indexing documents.  
  - **Searcher.java**: Allows searching in the index.  
  - **AnalyzeTest.java**: Test classes for analysis.
  
---

## Installation and Usage

### 1. Clone the Project

```bash
git clone https://github.com/StefaniaCri/Ro-Information-Retrieval-System.git
cd Ro-Information-Retrieval-System
```

### 2. Build the Project

Make sure to use Maven to build the project:
```bash
mvn project
```

### 3. Indexing Documents

Run this comand to index documents from an directory whose path is mentioned in <path to docs>:
```bash
java -jar target/docsearch-1.0-SNAPSHOT.jar -index -directory <path to docs>
```
### 4. Searching Documents

Run this comand to search the <keyword> in index.
```bash
java -jar target/docsearch-1.0-SNAPSHOT.jar -search -query <keyword>
```

### Common Issues
Diacritic Issue

If the analyzed text contains incorrect characters (ș or ț, instead of ş and ţ), the result may be wrong. It is better to use the romanian diacritics when searching.


### Resources
- [Apache Lucene Documentation](https://lucene.apache.org/core/)
- [Apache Tika Documentation](https://tika.apache.org/)
- [Snowball Stemmer Documentation](http://snowball.tartarus.org/)
- [GitHub Repository](https://github.com/StefaniaCri/Ro-Information-Retrieval-System)
