# Groovy Crawler for TISS data from Government Website

This is a project do collect TISS (Brazilian Standard for the Exchange of Supplementary Health Information) data from the [government website](https://www.gov.br/ans/pt-br/assuntos/prestadores).
It uses `jsoup` for collecting data from HTML pages and `HTTPBuilder-NG` for downloading files.

## Tecnologies

- Groovy
- Gradle
- Jsoup
- HttpBuilder-ng

## Functionalities

- Download zip of the most recent version of documentations of TISS pattern;
- Creation of .csv table with data of the versions' history of TISS documents;
- Download .xlsx table of error codes in data transmission to ANS (National Agency for Supplementary Health).

## How to run

```bash
./gradlew run
```

## Project Structure
```
.
├── src
│   └── main
│       └── groovy
│           └── org.example
│               ├── Crawler.groovy
│               └── CrawlerScript.groovy
├── downloads                            # Directory for downloaded/generated content
│   ├── padrao-tiss.zip
│   ├── tabela-erros-tiss.xlsx
│   └── historico.csv
```

## Author

Alex Benjamim de Oliveira Martins 
