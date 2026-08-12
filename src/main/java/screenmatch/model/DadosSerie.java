package screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo, @JsonAlias("Year") String ano,
		@JsonAlias("Genre") String genero, @JsonAlias("imdbRating") String avaliacao, @JsonAlias("Actors") String atores,
		@JsonAlias("Poster") String poster, @JsonAlias("Plot") String sinopse,	@JsonAlias("totalSeasons") Integer totalTemporadas) {
}