package br.com.bot.botapi.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.bot.botapi.dto.PartidaGoogleDTO;

public class ScrapingUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);
	
	private static final String BASE_URL_GOOGLE = "https://www.google.com.br/?search/q="; //https://www.google.com/?search/q=
	private static final String COMPLEMENTO_URL_GOOGLE = "&hl=pt-BR";
	
	public static void main(String[] args) {

		String complemento = "";
		String url = "https://cursos.dankicode.com/login"; //BASE_URL_GOOGLE + complemento; 
		
		ScrapingUtil scraping = new ScrapingUtil();
		scraping.obtemInformacoesPartida(url);
	}
	
	public PartidaGoogleDTO obtemInformacoesPartida(String url) {
		
		PartidaGoogleDTO partida = new PartidaGoogleDTO();
		
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			
			String title = document.title();
			LOGGER.info("Titulo da pagína: {}", title);
			
		} catch (IOException e) {
			LOGGER.error("ERRO AO CONECTAR NA URL COM JSOUP -> {}", e.getMessage());
		}
		
		return partida;
	}

}
