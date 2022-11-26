package br.com.bot.botapi.util;

import java.io.IOException;
import java.util.Scanner;

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

		Scanner sc = new Scanner(System.in);
		
		System.out.println("Informa a o jogo: ");
		String complemento = sc.nextLine().replace(" ", "+");
				
//				"argentina+x+méxico";
		
		String url = "https://www.google.com/search?q=" + complemento;
//		String url = "https://www.google.com/search?q=tun%C3%ADsia+x+austr%C3%A1lia";  
//		String url = BASE_URL_GOOGLE + complemento; 
		
		ScrapingUtil scraping = new ScrapingUtil();
		scraping.obtemInformacoesPartida(url);
		
		PartidaGoogleDTO par = new PartidaGoogleDTO();
		
	}
	
	public PartidaGoogleDTO obtemInformacoesPartida(String url) {
		
		PartidaGoogleDTO partida = new PartidaGoogleDTO();
		
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			
			String title = document.title();
			LOGGER.info("Titulo da pagína: {}", title);
			
			String partidaStatus = obtemStatusPartida(document).toString();
			String partidaTempo = obtemTempoPartida(document);
			
//			LOGGER.info("Status: {}", partid);
			
		} catch (IOException e) {
			LOGGER.error("ERRO AO CONECTAR NA URL COM JSOUP -> {}", e.getMessage());
		}
		
		return partida;
	}
	
	public StatusPartida obtemStatusPartida(Document document) {
		
		StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;
		
		boolean isTempoPardida = document.select("div[class=imso_mh__lv-m-stts-cont]").isEmpty();
		
		if(!isTempoPardida) {
			String tempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").first().text();
			statusPartida = StatusPartida.PARTIDA_EM_ANDAMENTO;
			if(tempoPartida.contains("Pênaltis")) {
				statusPartida = StatusPartida.PARTIDA_PENATIS;
			}
			LOGGER.info(tempoPartida);
		}
		
		isTempoPardida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
		
		if(!isTempoPardida) {
			statusPartida = StatusPartida.PARTIDA_ENCERRADA;
		}
		LOGGER.info(statusPartida.toString());
		return statusPartida;

	}
	
	public String obtemTempoPartida(Document document) {
		
		String tempoPartida = null;
		
		boolean isTempoPardida = document.select("div[class=imso_mh__lv-m-stts-cont]").isEmpty();
		
		if(!isTempoPardida) {
			tempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").first().text();
		}
		
		isTempoPardida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
		if(!isTempoPardida) {
			tempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").first().text();
		}
		LOGGER.info(tempoPartida);
		return tempoPartida.replace("'", "min");
	}

}
