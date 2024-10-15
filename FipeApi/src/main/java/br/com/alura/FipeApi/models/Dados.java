package br.com.alura.FipeApi.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Dados(@JsonAlias("codigo") String codigoDaMarca,
                    @JsonAlias("nome") String nomeDaMarca) {
}
