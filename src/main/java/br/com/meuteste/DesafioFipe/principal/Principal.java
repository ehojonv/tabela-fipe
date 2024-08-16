package br.com.meuteste.DesafioFipe.principal;

import br.com.meuteste.DesafioFipe.models.Dados;
import br.com.meuteste.DesafioFipe.models.Marca;
import br.com.meuteste.DesafioFipe.models.Veiculo;
import br.com.meuteste.DesafioFipe.service.ConsumoApi;
import br.com.meuteste.DesafioFipe.service.ConverteDados;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados convertor = new ConverteDados();

    public void exibirMenu() throws IOException, InterruptedException {
        //Primeiro menu
        System.out.println("""
                Bem vindo ao procurador de preço de veiculos!\

                1 - Carros\

                2 - Motos\

                3 - Caminhões\

                Digite o numero do tipo de veiculo para a busca:""");
        try {

            int busca;
            if (scanner.hasNextInt()) {
                busca = scanner.nextInt();
            } else {
                throw new RuntimeException("Valor inválido");
            }

            String procura1;
            if (busca == 1) {
                procura1 = "carros";
            } else if (busca == 2) {
                procura1 = "motos";
            } else if (busca == 3) {
                procura1 = "caminhoes";
            } else {
                throw new RuntimeException("Número inválido");
            }

            var tipoVeiculo = procura1 + "/marcas/";

            var json = consumo.obterDados(ENDERECO + tipoVeiculo);
            List<Dados> dadosList = convertor.obterDados(json, new TypeReference<>() {
            });
            dadosList.stream()
                    .sorted(Comparator.comparing(Dados::nome))
                    .forEach(v -> System.out.println("Marca: " + v.nome() + " | Cod: " + v.codigo()));

            System.out.println("Escreva o código da marca desejada:");

            //exibe os modelos da determinada marca e seu respectivo codigo

            int procura2;
            if (scanner.hasNextInt()) {
                procura2 = scanner.nextInt();
            } else {
                throw new RuntimeException("Valor inválido");
            }

            var marcaProcurada = procura2 + "/modelos/";
            json = consumo.obterDados(ENDERECO + tipoVeiculo + marcaProcurada);
            Marca marca = convertor.obterDados(json, new TypeReference<>() {});
            if (marca.modelos() == null) {
                throw new RuntimeException("Código inválido");
            }
            marca.modelos().stream()
                    .sorted(Comparator.comparing(Dados::nome))
                    .forEach(m -> System.out.println("Modelo: " + m.nome() + " | Cod: " + m.codigo()));

            System.out.println("Escreva o código do modelo desejado:");

            int procura3;
            if (scanner.hasNextInt()) {
                procura3 = scanner.nextInt();
            } else {
                throw new RuntimeException("Valor inválido");
            }

            var modelo = procura3 + "/anos/";
            json = consumo.obterDados(ENDERECO + tipoVeiculo + marcaProcurada + modelo);
            List<Dados> dadosModeloList = convertor.obterDados(json, new TypeReference<>() {});
            if (dadosModeloList == null) {
                throw new RuntimeException("Código inválido");
            }
            dadosModeloList.stream()
                    .sorted(Comparator.comparing(Dados::nome))
                    .forEach(m -> {
                        try {
                            String json1 = consumo.obterDados(ENDERECO + tipoVeiculo + marcaProcurada + modelo + m.codigo());
                            Veiculo veiculo = convertor.obterDados(json1, new TypeReference<>() {
                            });
                            System.out.println("Valor: " + veiculo.valor() + " | Modelo: " + veiculo.modelo() +
                                    " | Marca: " + veiculo.marca() + " | Ano: " + veiculo.anoModelo());
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
