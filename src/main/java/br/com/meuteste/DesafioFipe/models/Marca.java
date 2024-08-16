package br.com.meuteste.DesafioFipe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Marca(List<Dados> modelos) {
}
