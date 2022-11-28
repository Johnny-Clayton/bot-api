package br.com.bot.botapi.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.bot.botapi.dto.PartidaGoogleDTO;

public class ScrapingUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);
	
	private static final String BASE_URL_GOOGLE = "https://www.google.com.br/?search/q="; 
	private static final String COMPLEMENTO_URL_GOOGLE = "&hl=pt-BR";
	
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		System.out.println("Informa a o jogo: ");
		String complemento = sc.nextLine().replace(" ", "+");
				
//		argentina x méxico
//		camarões x sérvia
		
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
			LOGGER.info("Titulo da Partida: {}", title);
			
			StatusPartida statusPartida = obtemStatusPartida(document);
			LOGGER.info("Status da Partida: {}",statusPartida);
			
			String nomeEquipeCasa = recuperaNomeEquipeCasa(document);
//			LOGGER.info("Nome da Equipe da Casa: {}",nomeEquipeCasa);
			
			String nomeEquipeVisitante = recuperaNomeEquipeVisitante(document);
//			LOGGER.info("Nome da Equipe da Visitante: {}",nomeEquipeVisitante);
			
			if(statusPartida != StatusPartida.PARTIDA_NAO_INICIADA) {
				String tempoPartida = obtemTempoPartida(document);
				LOGGER.info("Tempo da Partida: {}",tempoPartida);
				
				Integer placarDaCasa = placarDaCasa(document);
				LOGGER.info("Placar do(a) "+ nomeEquipeCasa + ": {}", placarDaCasa);
				
				Integer placarDoVisitante = placarDoVisitante(document);
				LOGGER.info("Placar do(a) "+ nomeEquipeVisitante +": {}", placarDoVisitante);
				
			}
			String golsEquipeCasa = recuperaGolsEquipeCasa(document);
			LOGGER.info("Gols do(a) "+ nomeEquipeCasa + ": {}",golsEquipeCasa);
			
			String golsEquipeVisitante = recuperaGolsEquipeVisitante(document);
			LOGGER.info("Gols do(a) "+ nomeEquipeVisitante + ": {}",golsEquipeVisitante);
			
			String dataDaPartida = recuperaDataDaPartida(document);
			LOGGER.info("Data da Partida: {}",dataDaPartida);
			
			String urlLogoEquipeCasa = recuperaLogoEquipeCasa(document);
			LOGGER.info("Url logo equipe Casa: {}",urlLogoEquipeCasa);
			
			String urlLogoEquipeVisitante = recuperaLogoEquipeVisitante(document);
			LOGGER.info("Url logo equipe Visitante: {}",urlLogoEquipeVisitante);
			
			
			
			
			
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
		}
		
		isTempoPardida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
		
		if(!isTempoPardida) {
			statusPartida = StatusPartida.PARTIDA_ENCERRADA;
		}
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
		return corrigrTempoPartida(tempoPartida);
	}
	
	public String corrigrTempoPartida(String tempo) {
		if(tempo.contains("'")) {
			return tempo = tempo.replace("'", " min");
		} else {
			return tempo;
		}	
	}
	
	public String recuperaNomeEquipeCasa(Document document) {
	
		Element elemento = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
		String nomeEquipe =elemento.select("span").text();
		
		return nomeEquipe;
	}
	
	public String recuperaNomeEquipeVisitante(Document document) {
		
		Element elemento = document.selectFirst("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]");
		String nomeEquipeVisitante =elemento.select("span").text();
		
		return nomeEquipeVisitante;
	}
	
	public String recuperaLogoEquipeCasa(Document document) {
		
		Element elemento = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
		String urlLogo = "https:" + elemento.select("img[class=imso_btl__mh-logo]").attr("src");
		//Arrumar link
		return urlLogo;
	}
	
	public String recuperaLogoEquipeVisitante(Document document) {
		
		Element elemento = document.selectFirst("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]");
		String urlLogoVisitante = "https:" + elemento.select("img[class=imso_btl__mh-logo]").attr("src");
		//Arrumar link
		return urlLogoVisitante;
	}
	
	public Integer placarDaCasa(Document document) {
		
		String placarDaCasa = document.selectFirst("div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]").text();
		
		return Integer.valueOf(placarDaCasa);
	}
	
	public Integer placarDoVisitante(Document document) {
		
		String placarDoVisitante = document.selectFirst("div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]").text();
		
		return Integer.valueOf(placarDoVisitante);
	}
	
	public String recuperaGolsEquipeCasa(Document document) {
		
		List<String> golsEquipe = new ArrayList<>();
		Elements elementos = document.select("div[class=imso_gs__tgs imso_gs__left-team]").select("div[class=imso_gs__gs-r]");
		for(Element e : elementos) {
			String infoGols = e.select("div[class=imso_gs__gs-r]").text();
			golsEquipe.add(infoGols);
		}
		if(golsEquipe.isEmpty()) {
			return "Sem Gol";
		}
		
		return String.join(", ", golsEquipe);
	}
	
	public String recuperaGolsEquipeVisitante(Document document) {
		
		List<String> golsEquipe = new ArrayList<>();
		Elements elementos = document.select("div[class=imso_gs__tgs imso_gs__right-team]").select("div[class=imso_gs__gs-r]");
		for(Element e : elementos) {
			String infoGols = e.select("div[class=imso_gs__gs-r]").text();
			golsEquipe.add(infoGols);
		}
		if(golsEquipe.isEmpty()) {
			return "Sem Gol";
		}
		
		return String.join(", ", golsEquipe);
	}
	
	public String recuperaDataDaPartida(Document document) {
	
		String dataPartida = document.select("div.imso_mh__stts-l.imso-ani.imso_mh__stts-l-cont > div > div > span:nth-child(2)").text();
		
		return dataPartida;
	}

}
