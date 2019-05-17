package com.starwars.api.models;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Planet {
    @Id
    private String id;

    @TextIndexed
    @NotEmpty(message = "Campo nome é obrigatório")
    private String nome;

    @NotEmpty(message = "Campo clima é obrigatório")
    private String clima;

    @NotEmpty(message = "Campo terreno é obrigatório")
    private String terreno;

    @Transient
    private Integer aparicoes;

    public Planet() {

    }

    public Planet(String nome, String clima, String terreno) {
        this.nome = nome;
        this.clima = clima;
        this.terreno = terreno;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getClima() {
		return clima;
	}

	public void setClima(String clima) {
		this.clima = clima;
	}

	public String getTerreno() {
		return terreno;
	}

	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}

	public Integer getAparicoes() {
		return aparicoes;
	}

	public void setAparicoes(Integer aparicoes) {
		this.aparicoes = aparicoes;
	}

}