package screenmatch.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import screenmatch.model.Categoria;
import screenmatch.model.DadosSerie;
import screenmatch.model.DadosTemporada;
import screenmatch.model.Episodio;
import screenmatch.model.Serie;
import screenmatch.repository.SerieRepository;
import screenmatch.service.ConsumoApi;
import screenmatch.service.ConverteDados;

public class PrincipalComLista {

	private Scanner leitura = new Scanner(System.in);
	private ConsumoApi consumo = new ConsumoApi();
	private ConverteDados conversor = new ConverteDados();
	private final String ENDERECO = "https://www.omdbapi.com/?t=";
	private final String API_KEY = "&apikey="SUA API KEY"";
	private List<DadosSerie> dadosSeries = new ArrayList<>(); 
	private SerieRepository repositorio;
	private List<Serie> series = new ArrayList<>();
	private Optional<Serie> serieBusca; 


	public PrincipalComLista(SerieRepository repositorio) {
		this.repositorio = repositorio;
	}

	public void exibeMenu() {

		int opcao = -1;
		while (opcao != 0) {
			var menu = """
					\n
					1 - Buscar séries
					2 - Buscar episódios
					3 - Listar séries buscadas
					4 - Buscar série por título
					5 - Buscar série por ator(atriz)
					6 - Top 5 séries
					7 - Buscar séries por categoria
					8 - Filtrar séries
					9 - Buscar episódio por trecho
					10 - Top 5 episódios da serie
					11 - Buscar episódios por ano de lançamento

					0 - Sair 
					""";

			System.out.println(menu);
			opcao = leitura.nextInt();
			leitura.nextLine();

			switch (opcao) {
			case 1:
				buscarSerieWeb();
				break;
			case 2:
				buscarEpisodioPorSerie();
				break;
			case 3:
				listarSeriesBuscadas();
				break;
			case 4:
				buscarSeriePorTitulo();
				break;
			case 5:
				buscarSeriePorAtor();
				break;
			case 6:
				buscarTop5Series();
				break;
			case 7:
				buscarSeriesPorCategoria();
				break;
			case 8: 
				filtrarSeriesPorTemporadaEAvaliacao();
				break;
			case 9:
				buscarEpisodioPorTrecho();
				break;
			case 10:
				topEpisodiosPorSerie();
				break;
			case 11:
				buscarEpisodiosPorData();
				break;
			case 0:
				System.out.println("Saindo...");
				break;
			default:
				System.out.println("Opção inválida");
			}
		}
	}

	private void buscarSerieWeb() {
    	DadosSerie dados = getDadosSerie();
    	try {
        	Serie serie = new Serie(dados);
        	repositorio.save(serie);
        	System.out.println(dados);
    	} catch (NullPointerException | IllegalArgumentException e) {
        	System.out.println("\nNenhuma categoria encontrada para a série (ou dados incompletos/ausentes na API).");
    	}
}

	private DadosSerie getDadosSerie() {
		System.out.println("\nDigite o nome da série para busca");
		var nomeSerie = leitura.nextLine();
		var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		return dados;
	}

	private void buscarEpisodioPorSerie() {
		System.out.println("\nSéries disponiveis:");
		listarSeriesBuscadas();
		System.out.println("\nEscolha uma série pelo nome: ");
		var nomeSerie = leitura.nextLine();

		Optional<Serie> serie = series.stream()
				.filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
				.findFirst();

		if (serie.isPresent()){

			var serieEncontrada = serie.get();
			List<DadosTemporada> temporadas = new ArrayList<>();
			
			for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
			var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

		List<Episodio> episodios = temporadas.stream()
				.flatMap(d -> d.episodios().stream()
						.map( e -> new Episodio(d.temporada(), e)))
				.collect(Collectors.toList()); 
		serieEncontrada.setEpisodios(episodios);
		repositorio.save(serieEncontrada);

		}else {
			System.out.println("\nSérie não encontrada.");
		}
	}

	private void listarSeriesBuscadas() {
		series = new ArrayList<>();
		series = repositorio.findAll();
		series.stream()
					.sorted(Comparator.comparing(Serie::getGenero))
					.forEach(System.out::println);
	}

	private void buscarSeriePorTitulo() {
		System.out.println("\nEscolha uma série pelo título: ");
		var nomeSerie = leitura.nextLine();
		serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

		if(serieBusca.isPresent()){
			System.out.println("\nDados da série: " + serieBusca.get());
		}else {
			System.out.println("\nSérie não encontrada.");
		}
	}

	private void buscarSeriePorAtor() {
		System.out.println("\nEscolha a serie pelo nome do ator(a): ");
		var nomeAtor = leitura.nextLine();
		System.out.println("\n");
		System.out.println("Avaliação mínima da série: ");
		var avaliacao = leitura.nextDouble();
		List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
		System.out.println("\nSéries em que " + nomeAtor + " trabalhou:");
		seriesEncontradas.forEach (s -> 
				System.out.println("\nTítulo: " + s.getTitulo() + " avaliação: " + s.getAvaliacao()));
		
	}

	private void buscarTop5Series() {
		System.out.println("\n");
		List <Serie> serieTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
		serieTop.forEach(s -> 
				System.out.println("\nTítulo: " + s.getTitulo() + " | Avaliação: " + s.getAvaliacao()));
	}

	private void buscarSeriesPorCategoria() {
		System.out.println("\nQual gênero de séries deseja assistir: ");
		var nomeGenero = leitura.nextLine(); 
		Categoria categoria = Categoria.fromPortugues(nomeGenero);
		List <Serie> seriesPorCategoria = repositorio.findByGenero(categoria);	
		System.out.print("\nSéris da categoria: " + nomeGenero + "disponíveis: ");
		seriesPorCategoria.forEach(System.out::println);	
	}

	private void filtrarSeriesPorTemporadaEAvaliacao() {
		System.out.println("\nFiltrar séries até quantas temporadas? ");
		var totalTemporadas = leitura.nextInt();
		System.out.println("\nCom avaliação a partir de que valor? ");
		var avaliacao = leitura.nextDouble();
		leitura.nextLine();
		List <Serie> filtroSeries = repositorio.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporadas, avaliacao);
		System.out.println("\nSéries filtradas: ");
		filtroSeries.forEach(s ->
			System.out.println("\nTítulo: " + s.getTitulo() + " avaliação: " + s.getAvaliacao()));
	}

	private void buscarEpisodioPorTrecho() {
		System.out.println("Qual nome do episódio deseja buscar? ");
		var trechoEpisodio = leitura.nextLine();
		System.out.println("\n");
		List <Episodio> episodiosEncontrados = repositorio.episodioPorTrecho(trechoEpisodio);
		episodiosEncontrados.forEach(e -> 
				System.out.printf("Série: %s | Temporada: %s | Episódio: %s - %s\n",
					e.getSerie().getTitulo(), e.getTemporada(),
					e.getNumeroEpisodio(), e.getTitulo()));
	}

	private void topEpisodiosPorSerie() {
		buscarSeriePorTitulo();
		if (serieBusca.isPresent()){
			Serie serie = serieBusca.get();
			List <Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie); 
			System.out.println("\nTop 5 episódios: ");
			topEpisodios.forEach(e-> 
				System.out.printf("\nSérie: %s | Temporada: %s | Episódio: %s | %s Avaliação- %s\n",
					e.getSerie().getTitulo(), e.getTemporada(),
					e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
		}
	}

	private void buscarEpisodiosPorData() {
		buscarSeriePorTitulo();
		if(serieBusca.isPresent()){
			Serie serie =serieBusca.get();
			System.out.println("\nDigite o ano limite de lançamento: ");
			var anoDeLancamento = leitura.nextInt();
			leitura.nextLine();

			List <Episodio> episodiosAno = repositorio.episodioPorSerieEAno(serie, anoDeLancamento);
			System.out.println("\nEpisódios encontrados: \n");
			episodiosAno.forEach(System.out::println);
		}
	}
}
