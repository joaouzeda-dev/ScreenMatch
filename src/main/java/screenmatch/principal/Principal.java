package screenmatch.principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import screenmatch.model.DadosEpisodio;
import screenmatch.model.DadosSerie;
import screenmatch.model.DadosTemporada;
import screenmatch.model.Episodio;
import screenmatch.service.ConsumoApi;
import screenmatch.service.ConverteDados;

public class Principal {

	private Scanner leitura = new Scanner(System.in);
	private ConsumoApi consumoApi = new ConsumoApi();
	private ConverteDados converteDados = new ConverteDados();
	private final String ENDERECO = "https://www.omdbapi.com/?t=";
	private final String API_KEY = "&apikey=d9042b6a";

	public void exibeMenu() {
		System.out.println("Digite o nome da série que deseja pesquisar: ");
		var nomeSerie = leitura.nextLine();
		System.out.println("Você digitou: " + nomeSerie);
		System.out.println("\nDados da série: \n");
		var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
		DadosSerie dados = converteDados.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		System.out.println("\nDados das temporadas: \n");
		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++) {
			json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

		System.out.println("\nLista sequencial com os nomes de todos os episódios: \n");
//			for (int i =0; i < dados.totalTemporadas(); i++) {
//				List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//				for (int j = 0; j < episodiosTemporada.size(); j++) {
//					System.out.println(episodiosTemporada.get(j).titulo()); 
//				}
//			Mesma coisa que o código acima, mas usando forEach:
		temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

		System.out.println("\n");

		List<DadosEpisodio> dadosEpisodios = temporadas.stream().flatMap(t -> t.episodios().stream())
				.collect(Collectors.toList());

		System.out.println("\nTop 10 episódios com melhor avaliação:\n");
		dadosEpisodios.sort(Comparator.comparing(DadosEpisodio::avaliacao).reversed());
		dadosEpisodios.stream().filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
				.peek(e -> System.out.println("Primeiro filtro (N/A) | " + e)).limit(10)
				.peek(e -> System.out.println("Segundo filtro: limite | " + e)).map(e -> e.titulo().toUpperCase())
				.peek(e -> System.out.println("Terceiro filtro: mapeamento | " + e + "\n")).collect(Collectors.toList())
				.forEach(System.out::println);

		System.out.println("\nLista de episódios com data de lançamento: \n");

		List<Episodio> episodios = temporadas.stream()
				.flatMap(t -> t.episodios().stream().map(d -> new Episodio(t.temporada(), d)))
				.collect(Collectors.toList());

		episodios.forEach(System.out::println);

		System.out.println("\nDigite um trecho do título do episódio que deseja buscar: \n");
		var trechoTitulo = leitura.nextLine();
		System.out.println("\nVocê digitou: " + trechoTitulo);
		List<Episodio> episodioBuscado = episodios.stream()
				.filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
				.collect(Collectors.toList());
		if (!episodioBuscado.isEmpty()) {
			System.out.println("\nEpisódios encontrados: ");
			episodioBuscado.forEach(System.out::println);
		} else {
			System.out.println("Nenhum episódio encontrado com o trecho: " + trechoTitulo);
		}

		System.out.println("\n A partir de que ano você deseja filtrar os episódios? \n");
		var ano = leitura.nextInt();
		System.out.println("\nEpisodios filtrados a partir do ano: " + ano + "\n");

		LocalDate dataBusca = LocalDate.of(ano, 1, 1);

		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		episodios.stream().filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
				.forEach(e -> System.out.println("Temporada: " + e.getTemporada() + " | Episódio: " + e.getTitulo()
						+ " | Data de Lançamento: " + e.getDataLancamento().format(formatador)));

		Map<Integer, Double> avaliacoesPorTemporada = episodios.stream().filter(e -> e.getAvaliacao() > 0).collect(
				Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));
		System.out.println("\nAvaliação média por temporada: \n");
		System.out.println(avaliacoesPorTemporada);

		DoubleSummaryStatistics est = episodios.stream().filter(e -> e.getAvaliacao() > 0.0)
				.collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
		System.out.println("\nEstatísticas de avaliação dos episódios: \n");
		System.out.println("Média das avaliações: " + est.getAverage());
		System.out.println("Melhor avaliação: " + est.getMax());
		System.out.println("Pior avaliação: " + est.getMin());
		System.out.println("Total de episódios avaliados: " + est.getCount());

	}

}
