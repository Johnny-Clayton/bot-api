package br.com.bot.botapi.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaGoogleDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String statusPartida;
	private String tempoPartida;
	
	//into da Casa
	private String nomeEquipeCasa;
	private String urlLogoEquipeCasa;
	private Integer placarEquipeCasa;
	private String golsEquipeCasa;
	private String placarEstendidoEquipeCasa;

	//into da Visitante
	private String nomeEquipeVisitante;
	private String urlLogoEquipeVisitante;
	private Integer placarEquipeVisitante;
	private String golsEquipeVisitante;
	private String placarEstendidoEquipeVisitante;
	
	public String getStatusPartida() {
		return statusPartida;
	}
	public void setStatusPartida(String statusPartida) {
		this.statusPartida = statusPartida;
	}
	public String getTempoPartida() {
		return tempoPartida;
	}
	public void setTempoPartida(String tempoPartida) {
		this.tempoPartida = tempoPartida;
	}
	public String getNomeEquipeCasa() {
		return nomeEquipeCasa;
	}
	public void setNomeEquipeCasa(String nomeEquipeCasa) {
		this.nomeEquipeCasa = nomeEquipeCasa;
	}
	public String getUrlLogoEquipeCasa() {
		return urlLogoEquipeCasa;
	}
	public void setUrlLogoEquipeCasa(String urlLogoEquipeCasa) {
		this.urlLogoEquipeCasa = urlLogoEquipeCasa;
	}
	public Integer getPlacarEquipeCasa() {
		return placarEquipeCasa;
	}
	public void setPlacarEquipeCasa(Integer placarEquipeCasa) {
		this.placarEquipeCasa = placarEquipeCasa;
	}
	public String getGolsEquipeCasa() {
		return golsEquipeCasa;
	}
	public void setGolsEquipeCasa(String golsEquipeCasa) {
		this.golsEquipeCasa = golsEquipeCasa;
	}
	public String getPlacarEstendidoEquipeCasa() {
		return placarEstendidoEquipeCasa;
	}
	public void setPlacarEstendidoEquipeCasa(String placarEstendidoEquipeCasa) {
		this.placarEstendidoEquipeCasa = placarEstendidoEquipeCasa;
	}
	public String getNomeEquipeVisitante() {
		return nomeEquipeVisitante;
	}
	public void setNomeEquipeVisitante(String nomeEquipeVisitante) {
		this.nomeEquipeVisitante = nomeEquipeVisitante;
	}
	public String getUrlLogoEquipeVisitante() {
		return urlLogoEquipeVisitante;
	}
	public void setUrlLogoEquipeVisitante(String urlLogoEquipeVisitante) {
		this.urlLogoEquipeVisitante = urlLogoEquipeVisitante;
	}
	public Integer getPlacarEquipeVisitante() {
		return placarEquipeVisitante;
	}
	public void setPlacarEquipeVisitante(Integer placarEquipeVisitante) {
		this.placarEquipeVisitante = placarEquipeVisitante;
	}
	public String getGolsEquipeVisitante() {
		return golsEquipeVisitante;
	}
	public void setGolsEquipeVisitante(String golsEquipeVisitante) {
		this.golsEquipeVisitante = golsEquipeVisitante;
	}
	public String getPlacarEstendidoEquipeVisitante() {
		return placarEstendidoEquipeVisitante;
	}
	public void setPlacarEstendidoEquipeVisitante(String placarEstendidoEquipeVisitante) {
		this.placarEstendidoEquipeVisitante = placarEstendidoEquipeVisitante;
	}
	
	public PartidaGoogleDTO() {
		super();
		
	}
	public PartidaGoogleDTO(String statusPartida, String tempoPartida, String nomeEquipeCasa, String urlLogoEquipeCasa,
			Integer placarEquipeCasa, String golsEquipeCasa, String placarEstendidoEquipeCasa,
			String nomeEquipeVisitante, String urlLogoEquipeVisitante, Integer placarEquipeVisitante,
			String golsEquipeVisitante, String placarEstendidoEquipeVisitante) {
		super();
		this.statusPartida = statusPartida;
		this.tempoPartida = tempoPartida;
		this.nomeEquipeCasa = nomeEquipeCasa;
		this.urlLogoEquipeCasa = urlLogoEquipeCasa;
		this.placarEquipeCasa = placarEquipeCasa;
		this.golsEquipeCasa = golsEquipeCasa;
		this.placarEstendidoEquipeCasa = placarEstendidoEquipeCasa;
		this.nomeEquipeVisitante = nomeEquipeVisitante;
		this.urlLogoEquipeVisitante = urlLogoEquipeVisitante;
		this.placarEquipeVisitante = placarEquipeVisitante;
		this.golsEquipeVisitante = golsEquipeVisitante;
		this.placarEstendidoEquipeVisitante = placarEstendidoEquipeVisitante;
	}
}
