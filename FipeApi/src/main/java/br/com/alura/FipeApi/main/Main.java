package br.com.alura.FipeApi.main;

import br.com.alura.FipeApi.models.Dados;
import br.com.alura.FipeApi.models.Modelos;
import br.com.alura.FipeApi.models.Veiculo;
import br.com.alura.FipeApi.service.ConsumoApi;
import br.com.alura.FipeApi.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO ="https://parallelum.com.br/fipe/api/v1";


    public void exibirMenu(){
        System.out.println("Insira a opcao desejada para buscar veiculos:");
        System.out.println("1 - Carro");
        System.out.println("2 - Moto");
        System.out.println("3 - Caminhao");
        int opcao = leitura.nextInt();
        String json;
            switch (opcao) {
                case 1:
                    json = consumo.obterDados(ENDERECO+"/carros/marcas");
                    System.out.println(json);
                    List<Dados> marcas = conversor.obterLista(json, Dados.class);
                    marcas.stream()
                            .peek(d -> System.out.println("Comparando os codigos e ordenando..."))
                            // .sorted(Comparator.comparing(Dados::codigoDaMarca))  - `codigoDaMarca` nao ordenado devido ao tipo recebido da API (String)
                            .sorted(Comparator.comparingInt(d -> {
                                try {
                                    return Integer.parseInt(d.codigoDaMarca());
                                }catch (NumberFormatException e) {
                                    return Integer.MAX_VALUE;
                                }
                            }))// + desta forma, atraves do comparingInt(d->...), e possivel ordenar devido a conversao de String para Integer
                            .peek(d -> System.out.println("Imprimindo marca: "+d.nomeDaMarca()))
                            .forEach(System.out::println);

                    System.out.println("Porfavor, insira o codigo da marca desejada:");
                    int codigoInserido = leitura.nextInt();
                    json = consumo.obterDados(ENDERECO+"/carros/marcas/"+codigoInserido+"/modelos");
                    System.out.println(json);
                    Modelos listaModelos = conversor.obterDados(json,Modelos.class);
                    listaModelos.modelos().stream()
                            .peek(m-> System.out.println("Comparando os nomes dos modelos e ordenando..."))
                            .sorted(Comparator.comparing(Dados::nomeDaMarca))
                            .peek(m-> System.out.println("Imprimindo modelo:"+m.nomeDaMarca()))
                            .forEach(System.out::println);

                    leitura.nextLine();
                    System.out.println("\nInforme o NOME do carro desejado:");
                    var nomeInserido = leitura.nextLine();
                    List<Dados> modelosFiltrados = listaModelos.modelos().stream()
                            .filter(m-> m.nomeDaMarca().toLowerCase().contains(nomeInserido.toLowerCase()))
                            .collect(Collectors.toList());
                    System.out.println("Modelos filtrados:");
                    modelosFiltrados.forEach(System.out::println);

                    System.out.println("Digite o codigo do modelo:");
                    var codigoModelo = leitura.nextLine();
                    json = consumo.obterDados(ENDERECO+"/carros/marcas/"+codigoInserido+"/modelos/"+codigoModelo+"/anos");
                    List<Dados> anos = conversor.obterLista(json,Dados.class);
                    List<Veiculo> veiculos = new ArrayList<>();
                    for (int i = 0; i <anos.size() ; i++) {
                        var enderecoAnos = ENDERECO+"/carros/marcas/"+codigoInserido+"/modelos/"+codigoModelo+"/anos"+"/"+anos.get(i).codigoDaMarca();
                        json = consumo.obterDados(enderecoAnos);
                        Veiculo veiculo = conversor.obterDados(json,Veiculo.class);
                        veiculos.add(veiculo);
                    }
                    System.out.println("\nTodos os veiculos filtrados com avaliacoes por ano:");
                    veiculos.forEach(System.out::println);
                    break;
                case 2:
                    json = consumo.obterDados(ENDERECO+"/motos/marcas");
                    List<Dados> marcasMotos = conversor.obterLista(json, Dados.class);
                    marcasMotos.stream()
                            .peek(d -> System.out.println("Comparando os codigos e ordenando..."))
                            .sorted(Comparator.comparingInt(d -> {
                                try {
                                    return Integer.parseInt(d.codigoDaMarca());
                                }catch (NumberFormatException e) {
                                    return Integer.MAX_VALUE;
                                }
                            }))
                            .peek(d -> System.out.println("Imprimindo marca: "+d.nomeDaMarca()))
                            .forEach(System.out::println);

                    System.out.println("Porfavor, insira o codigo da marca desejada:");
                    int codigoInseridoMotos = leitura.nextInt();
                    json = consumo.obterDados(ENDERECO+"/motos/marcas/"+codigoInseridoMotos+"/modelos");
                    Modelos listaModelosMotos = conversor.obterDados(json,Modelos.class);
                    listaModelosMotos.modelos().stream()
                            .peek(m-> System.out.println("Comparando os nomes dos modelos e ordenando..."))
                            .sorted(Comparator.comparing(Dados::nomeDaMarca))
                            .peek(m-> System.out.println("Imprimindo modelo:"+m.nomeDaMarca()))
                            .forEach(System.out::println);

                    leitura.nextLine();
                    System.out.println("\nInforme o NOME da moto desejada:");
                    var nomeInseridoMotos = leitura.nextLine();
                    List<Dados> modelosFiltradosMotos = listaModelosMotos.modelos().stream()
                            .filter(m-> m.nomeDaMarca().toLowerCase().contains(nomeInseridoMotos.toLowerCase()))
                            .collect(Collectors.toList());
                    System.out.println("Modelos filtrados:");
                    modelosFiltradosMotos.forEach(System.out::println);

                    System.out.println("Digite o codigo do modelo:");
                    var codigoModeloMoto = leitura.nextLine();
                    json = consumo.obterDados(ENDERECO+"/motos/marcas/"+codigoInseridoMotos+"/modelos/"+codigoModeloMoto+"/anos");
                    List<Dados> anosMotos = conversor.obterLista(json,Dados.class);
                    List<Veiculo> veiculosMotos = new ArrayList<>();
                    for (int i = 0; i <anosMotos.size() ; i++) {
                        var enderecoAnos = ENDERECO+"/motos/marcas/"+codigoInseridoMotos+"/modelos/"+codigoModeloMoto+"/anos"+"/"+anosMotos.get(i).codigoDaMarca();
                        json = consumo.obterDados(enderecoAnos);
                        Veiculo veiculo = conversor.obterDados(json,Veiculo.class);
                        veiculosMotos.add(veiculo);
                    }
                    System.out.println("\nTodos os veiculos filtrados com avaliacoes por ano:");
                    veiculosMotos.forEach(System.out::println);
                    break;
                case 3:
                    String link = ENDERECO+"/caminhoes/marcas/";
                    json = consumo.obterDados(link);
                    List<Dados> marcasCaminhao = conversor.obterLista(json,Dados.class);
                    marcasCaminhao.stream()
                            .peek(d -> System.out.println("Comparando os codigos e ordenando..."))
                            .sorted(Comparator.comparingInt(d -> {
                                try {
                                    return Integer.parseInt(d.codigoDaMarca());
                                }catch (NumberFormatException e) {
                                    return Integer.MAX_VALUE;
                                }
                            }))
                            .peek(d -> System.out.println("Imprimindo marca: "+d.nomeDaMarca()))
                            .forEach(System.out::println);

                    System.out.println("Porfavor, insira o codigo da marca desejada:");
                    int codigoInseridoCaminhao = leitura.nextInt();
                    link = link + codigoInseridoCaminhao+"/modelos/";
                    json = consumo.obterDados(link);
                    Modelos listaModelosCaminhao = conversor.obterDados(json,Modelos.class);
                    listaModelosCaminhao.modelos().stream()
                            .peek(m-> System.out.println("Comparando os nomes dos modelos e ordenando..."))
                            .sorted(Comparator.comparing(Dados::nomeDaMarca))
                            .peek(m-> System.out.println("Imprimindo modelo:"+m.nomeDaMarca()))
                            .forEach(System.out::println);

                    leitura.nextLine();
                    System.out.println("\nInforme o NOME do caminhao desejado:");
                    var nomeInseridoCaminhao = leitura.nextLine();
                    List<Dados> modelosFiltradosCaminhao = listaModelosCaminhao.modelos().stream()
                            .filter(m-> m.nomeDaMarca().toLowerCase().contains(nomeInseridoCaminhao.toLowerCase()))
                            .collect(Collectors.toList());
                    System.out.println("Modelos filtrados:");
                    modelosFiltradosCaminhao.forEach(System.out::println);

                    System.out.println("Digite o codigo do modelo:");
                    var codigoModeloCaminhao = leitura.nextLine();
                    link = link + codigoModeloCaminhao +"/anos";
                    json = consumo.obterDados(link);
                    List<Dados> anosCaminhao = conversor.obterLista(json,Dados.class);
                    List<Veiculo> veiculosCaminhao = new ArrayList<>();
                    for (int i = 0; i <anosCaminhao.size() ; i++) {
                        var enderecoAnos = link+"/"+anosCaminhao.get(i).codigoDaMarca();
                        json = consumo.obterDados(enderecoAnos);
                        Veiculo veiculo = conversor.obterDados(json,Veiculo.class);
                        veiculosCaminhao.add(veiculo);
                    }
                    System.out.println("\nTodos os veiculos filtrados com avaliacoes por ano:");
                    veiculosCaminhao.forEach(System.out::println);

                    break;

            }
    }

}
