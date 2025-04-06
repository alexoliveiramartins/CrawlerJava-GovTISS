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
    def final outputDirPath = Paths.get('downloads')
    Document doc

    def outputFileTissName = 'padrao-tiss.zip'
    def outputFileTiss = outputDirPath.resolve(outputFileTissName).toFile()
    def outputFileTabelaName = 'tabela-erros-tiss.xlsx'
    def outputFileTabela = outputDirPath.resolve(outputFileTabelaName).toFile()

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
                Download.toFile(delegate, outputFileTiss)
            }
            println "======= Download completo: ${outputFileTiss.absolutePath} =======\n"
        } catch (Exception e) {
            println "Erro ao baixar o arquivo: " + e.message
        }
    }

    void downloadTabelasRelacionadas(){
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
        Elements tabelasRelacionadasLink = doc.select("a:contains(planilhas)")
        urlAtual = tabelasRelacionadasLink.attr('href')
        println "Redirecioando para $urlAtual..."

        try {
            doc = Jsoup.connect(urlAtual).get()
        } catch (IOException e){
            println "Erro ao acessar $urlAtual: " + e.message
        }
        Elements arquivoTabelaErros = doc.select("a:contains(Tabela de erros)")
        def urlZipTiss = arquivoTabelaErros.attr('href')
        println "$urlZipTiss"

        // baixar o arquivo
        try {
            HttpBuilder.configure {
                request.uri = urlZipTiss
            }.get {
                Download.toFile(delegate, outputFileTabela)
            }
            println "======= Download completo: ${outputFileTabela.absolutePath} =======\n"
        } catch (Exception e) {
            println "Erro ao baixar o arquivo: " + e.message
        }
    }

    void historyData(){
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
        Elements padraoTissUrl = doc.select("a:contains(acessar todas as)")
        urlAtual = padraoTissUrl.attr('href')
        println "Redirecioando para $urlAtual..."

        try{
            doc = Jsoup.connect(urlAtual).get()
        } catch (IOException e){
            println "Erro ao acessar $urlAtual: " + e.message
        }

        Elements listaHistorico = doc.select("tr")
        String arquivoCsv = 'historico.csv'
        String pathCsv = outputDirPath.resolve(arquivoCsv).toFile()
        try(FileWriter writer = new FileWriter(pathCsv)){
            listaHistorico.find {it  ->
                String csvLine = new String()
                it.children().each {
                    children ->
                        csvLine += children.text() + ","
                }
                if(csvLine.split(',')[0] == 'Jan/2016') {
                    def campos = csvLine.split(',')
                    writer.append(campos[0] + "," + campos[1] + "," + campos[2] + "\n")
                    return true
                }
                def campos = csvLine.split(',')
                writer.append(campos[0] + "," + campos[1] + "," + campos[2] + "\n")
                return false
            }
            println "======= Arquivo csv criado com sucesso =======\n"
        } catch (IOException e){
            println "Erro ao criar arquivo csv: " + e.message
        }
    }
}




