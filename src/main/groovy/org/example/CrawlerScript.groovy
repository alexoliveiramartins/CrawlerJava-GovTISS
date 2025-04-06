package org.example

def crawler = new Crawler()

// Download .zip dos aquivos da documentação do padrão TISS (Troca de Informações na Saúde Suplementar), na versão mais recente.
crawler.downloadTissZip()

// Dados .csv de competência, publicação e início de vigência a partir da competência de data jan/2016
crawler.historyData()

// Tabela .xlsx de erros no envio para a ANS
crawler.downloadTabelasRelacionadas()