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
import org.springframework.stereotype.Service;

import br.com.bot.botapi.dto.PartidaGoogleDTO;

@Service
public class ScrapingUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);
	
	private static final String BASE_URL_GOOGLE = "https://www.google.com.br/search?q="; 
	
	private static final String DIV_PARTIDA_ANDAMENTO = "div[class=imso_mh__lv-m-stts-cont]";
	private static final String DIV_PARTIDA_ENCERRADA = "span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]";
	
	private static final String DIV_DADOS_EQUIPE_CASA = "div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]";
	private static final String DIV_DADOS_EQUIPE_VISITANTE = "div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]";
	
	private static final String DIC_PLACAR_EQUIPE_CASA = "div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]";
	private static final String DIC_PLACAR_EQUIPE_VISITANTE = "div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]";
	
	private static final String DIC_GOLS_EQUIPE_CASA = "div[class=imso_gs__tgs imso_gs__left-team]";
	private static final String DIC_GOLS_EQUIPE_VISITANTE = "div[class=imso_gs__tgs imso_gs__right-team]";
	
	private static final String ITEM_GOL = "div[class=imso_gs__gs-r]";
	private static final String DIV_PENALIDADES = "div[class=imso_mh_s__psn-sc]";
	
	private static final String CASA = "casa"; 
	private static final String VISITANTE = "visitante";
	
//	public static void main(String[] args) {
//
//		Scanner sc = new Scanner(System.in);
//		
//		System.out.println("Informa a o jogo: ");
//		String complemento = sc.nextLine();		
//		
//		ScrapingUtil scraping = new ScrapingUtil();
//		String url = scraping.montaUrlGoogle(complemento);
//		scraping.obtemInformacoesPartida(url);
//		
//		PartidaGoogleDTO par = new PartidaGoogleDTO();
//		
//	}
	
	public PartidaGoogleDTO obtemInformacoesPartida(String url) {
		
		PartidaGoogleDTO partida = new PartidaGoogleDTO();
		
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			
			String title = document.title();
			LOGGER.info("Titulo da Partida: {}", title);
			
			StatusPartida statusPartida = obtemStatusPartida(document);
			partida.setStatusPartida(statusPartida.toString());
			LOGGER.info("Status da Partida: {}",statusPartida);
			
			String nomeEquipeCasa = recuperaNomeEquipe(document, DIV_DADOS_EQUIPE_CASA, 0);
			partida.setNomeEquipeCasa(nomeEquipeCasa);
			LOGGER.info("Equipe: {}",nomeEquipeCasa);
			
			String nomeEquipeVisitante = recuperaNomeEquipe(document, DIV_DADOS_EQUIPE_VISITANTE, 1);
			partida.setNomeEquipeVisitante(nomeEquipeVisitante);
			LOGGER.info("Equipe: {}",nomeEquipeVisitante);

			if(statusPartida != StatusPartida.PARTIDA_NAO_INICIADA) {
				String tempoPartida = obtemTempoPartida(document);
				partida.setTempoPartida(tempoPartida);
				LOGGER.info("Tempo da Partida: {}",tempoPartida);
				
				Integer placarDaCasa = placarDaEquipe(document, DIC_PLACAR_EQUIPE_CASA);
				partida.setPlacarEquipeCasa(placarDaCasa);
				LOGGER.info("Placar do(a) "+ nomeEquipeCasa + ": {}", placarDaCasa);
				
				Integer placarDoVisitante = placarDaEquipe(document, DIC_PLACAR_EQUIPE_VISITANTE);
				partida.setPlacarEquipeVisitante(placarDoVisitante);
				LOGGER.info("Placar do(a) "+ nomeEquipeVisitante +": {}", placarDoVisitante);
				
				Integer placarEstendidoEquipeCasa = buscaPenalidades(document, CASA); //nomeEquipeCasa
				partida.setPlacarEquipeCasa(placarEstendidoEquipeCasa);
				LOGGER.info("Pênaltis do(a) "+ nomeEquipeCasa +": {}", placarEstendidoEquipeCasa);
				
				Integer placarEstendidoEquipeVisitante = buscaPenalidades(document, VISITANTE); //nomeEquipeVisitante
				partida.setPlacarEquipeVisitante(placarEstendidoEquipeVisitante);
				LOGGER.info("Pênaltis do(a) "+ nomeEquipeVisitante +": {}", placarEstendidoEquipeVisitante);
			}

			String golsEquipeCasa = recuperaGolsEquipe(document, DIC_GOLS_EQUIPE_CASA);
			partida.setGolsEquipeCasa(golsEquipeCasa);
			LOGGER.info("Gols do(a) "+ nomeEquipeCasa + ": {}",golsEquipeCasa);
			
			String golsEquipeVisitante = recuperaGolsEquipe(document, DIC_GOLS_EQUIPE_VISITANTE);
			partida.setGolsEquipeVisitante(golsEquipeVisitante);
			LOGGER.info("Gols do(a) "+ nomeEquipeVisitante + ": {}",golsEquipeVisitante);
			
			String dataDaPartida = recuperaDataDaPartida(document);
			partida.setdataDaPartida(dataDaPartida);
			LOGGER.info("Data da Partida: {}",dataDaPartida);
			
//			String urlLogoEquipeCasa = recuperaLogoEquipe(document, DIV_DADOS_EQUIPE_CASA);
//			LOGGER.info("Url logo equipe Casa: {}",urlLogoEquipeCasa);
//			
//			String urlLogoEquipeVisitante = recuperaLogoEquipe(document, DIC_DADOS_EQUIPE_VISITANTE);
//			LOGGER.info("Url logo equipe Visitante: {}",urlLogoEquipeVisitante);
			return partida;
			
		} catch (IOException e) {
			LOGGER.error("ERRO AO CONECTAR NA URL COM JSOUP -> {}", e.getMessage());
		}
		
		return null;
	}
	
	public StatusPartida obtemStatusPartida(Document document) {
		
		StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;
		
		boolean isTempoPardida = document.select(DIV_PARTIDA_ANDAMENTO).isEmpty();
		
		if(!isTempoPardida) {
			String tempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).first().text();
			statusPartida = StatusPartida.PARTIDA_EM_ANDAMENTO;
			if(tempoPartida.contains("Pênaltis")) {
				statusPartida = StatusPartida.PARTIDA_PENATIS;
			}
		}
		
		isTempoPardida = document.select(DIV_PARTIDA_ENCERRADA).isEmpty();
		
		if(!isTempoPardida) {
			statusPartida = StatusPartida.PARTIDA_ENCERRADA;
		}
		return statusPartida;

	}
	
	public String obtemTempoPartida(Document document) {
		
		String tempoPartida = null;
		
		boolean isTempoPardida = document.select(DIV_PARTIDA_ANDAMENTO).isEmpty();
		
		if(!isTempoPardida) {
			tempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).first().text();
		}
		
		isTempoPardida = document.select(DIV_PARTIDA_ENCERRADA).isEmpty();
		if(!isTempoPardida) {
			tempoPartida = document.select(DIV_PARTIDA_ENCERRADA).first().text();
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
	
	public String recuperaNomeEquipe(Document document, String equipe, int numero) {
		
		Element elemento = document.selectFirst(equipe);
		
		if(elemento == null) {
			List<String> nome = pegarNomeDosTimes(document);
			String nomeEquipe = nome.get(numero);
			return nomeEquipe;
			
		} else {
			String nomeEquipe = elemento.select("span").text();

			return nomeEquipe;
		}
		
	}

	public String recuperaLogoEquipe(Document document, String equipe) {
		
		Element elemento = document.selectFirst(equipe);
		String urlLogo = "https:" + elemento.select("img[class=imso_btl__mh-logo]").attr("src");
		//Arrumar link
		return urlLogo;
	}

	public Integer placarDaEquipe(Document document, String equipe) {
		
		String placarDaCasa = document.selectFirst(equipe).text();
		
		return formaraPlacarStringInteger(placarDaCasa);
	}
	
	public String recuperaGolsEquipe(Document document, String equipe) {
		
		List<String> golsEquipe = new ArrayList<>();
		Elements elementos = document.select(equipe).select(ITEM_GOL);
		for(Element e : elementos) {
			String infoGols = e.select(ITEM_GOL).text();
			golsEquipe.add(infoGols);
		}
		if(golsEquipe.isEmpty()) {
			return "Sem Gol";
		}
		
		return String.join(", ", golsEquipe);
	}
	
	public List<String> pegarNomeDosTimes(Document document) {
		
		List<String> times = new ArrayList<>();
		Elements elementos = document.select("div > div[class=liveresults-sports-immersive__hide-element]");
		for(Element e : elementos) {
			String nomeDoTime = e.select("div[class=liveresults-sports-immersive__hide-element]").html();
			times.add(nomeDoTime);
		}
		
		return times;
	}
	
	public String recuperaDataDaPartida(Document document) {
	
		String dataPartida = document.select("div.imso_mh__stts-l.imso-ani.imso_mh__stts-l-cont > div > div > span:nth-child(2)").text();
		
		if(dataPartida.isEmpty()) {
			dataPartida = "Data Não Encontrada";
		}
		
		return dataPartida;
	}
	
	public Integer buscaPenalidades(Document document, String tipoEquipe) {
		
		boolean isPenalidades = document.select(DIV_PENALIDADES).isEmpty();
		if(!isPenalidades) {
			String penalidades = document.select(DIV_PENALIDADES).text();
			String penalidadesCompleta = penalidades.substring(0, 5).replace(" ", "");
			String[] divisao = penalidadesCompleta.split("-");
			
			return tipoEquipe.equals(CASA) ? formaraPlacarStringInteger(divisao[0]) : formaraPlacarStringInteger(divisao[1]);
		}
		
		return 0;
	}
	
	public Integer formaraPlacarStringInteger(String placar) {
		Integer valor;
		try {
			valor = Integer.parseInt(placar);
		} catch (Exception e) {
			valor = 0;
		}
		
		return valor;
	}
	
	public String montaUrlGoogle(String complemento) {
		try {
			String equipes = complemento.replace(" ", "+").replace("-", "+");
			
			return BASE_URL_GOOGLE + equipes;
		} catch (Exception e) {
			LOGGER.error("ERRO: {}", e.getMessage());
			return "ERRO!";
		}

	}

}
