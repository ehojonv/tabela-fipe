package br.com.meuteste.DesafioFipe.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Dados(String codigo,
                    String nome) {
}
