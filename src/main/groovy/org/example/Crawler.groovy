package org.example


import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.nio.file.Files
import java.nio.file.Paths

class Crawler {
    def baseUrl = "https://www.gov.br/ans/pt-br"
    Document doc

    def final outputDirPath = Paths.get('downloads')
    def outputFileName = 'padrao-tiss.zip'
    def outputFile = outputDirPath.resolve(outputFileName).toFile()

    void downloadTissZip(){
        def urlAtual

        try {
            Files.createDirectories(outputDirPath)
            println "Output directory ensured: ${outputDirPath.toAbsolutePath()}"
        } catch (IOException e) {
            println "Error creating output directory ${outputDirPath}: ${e.message}"
            System.exit(1)
        }

        try{
            doc = Jsoup.connect(baseUrl).get()
        } catch (IOException e){
            println "Erro ao acessar $baseUrl: " + e.message
        }
        Elements links = doc.select("a:contains(Prestador de Serviços de Saúde)")
        urlAtual = links.attr('href')
        println "Redirecionado para $urlAtual..."

        try{
            doc = Jsoup.connect(urlAtual).get()
        } catch (IOException e){
            println "Erro ao acessar $urlAtual: " + e.message
        }
        Elements tissLink = doc.select("a:contains(TISS)")
        urlAtual = tissLink.attr('href')
        println "Redirecionando para $urlAtual..."

        try{
            doc = Jsoup.connect(urlAtual).get()
        }catch (IOException e){
            println "Erro ao acessar $urlAtual: " + e.message
        }
        Elements padraoTissUrl = doc.select("a:contains(acessar a)")
        urlAtual = padraoTissUrl.attr('href')
        println "Redirecioando para $urlAtual..."

        try {
            doc = Jsoup.connect(urlAtual).get()
        } catch (IOException e){
            println "Erro ao acessar $urlAtual: " + e.message
        }
        Elements arquivoComponenteComunicacao = doc.select("a:contains(Componente de Comunicação)")
        def urlZipTiss = arquivoComponenteComunicacao.attr('href')
        println "$urlZipTiss"

        // baixar o arquivo
        try {
            HttpBuilder.configure {
                request.uri = urlZipTiss
            }.get {
                Download.toFile(delegate, outputFile)
            }
            println "Download completo: ${outputFile.absolutePath}"
        } catch (Exception e) {
            println "Erro ao baixar o arquivo: " + e.message
        }
    }
}




