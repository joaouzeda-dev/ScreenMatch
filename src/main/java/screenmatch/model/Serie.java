package screenmatch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "series")
public class Serie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String titulo;
	private String ano;
	@Enumerated(EnumType.STRING)
	private Categoria genero;
	private Double avaliacao; 
	private String atores;
	private String poster;
	private String sinopse;
	private Integer totalTemporadas;
	
	@OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Episodio> episodios = new ArrayList<>();

	public Serie() {}
	
	public Serie(DadosSerie dadosSerie) {
		this.titulo = dadosSerie.titulo();
		this.totalTemporadas = dadosSerie.totalTemporadas();
		this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
		this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
		this.ano = dadosSerie.ano();
		this.atores = dadosSerie.atores();
		this.poster = dadosSerie.poster();
		this.sinopse = dadosSerie.sinopse();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Episodio> getEpisodios() {
		return episodios;
	}
	

	public void setEpisodios(List<Episodio> episodios) {
		episodios.forEach(e -> e.setSerie(this));
		this.episodios = episodios;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Categoria getGenero() {
		return genero;
	}

	public void setGenero(Categoria genero) {
		this.genero = genero;
	}

	public Double getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Double avaliacao) {
		this.avaliacao = avaliacao;
	}

	public String getAtores() {
		return atores;
	}

	public void setAtores(String atores) {
		this.atores = atores;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}

	public Integer getTotalTemporadas() {
		return totalTemporadas;
	}

	public void setTotalTemporadas(Integer totalTemporadas) {
		this.totalTemporadas = totalTemporadas;
	}

	@Override
	public String toString() {
		return "Serie | Titulo = " + titulo + 
			   "| Ano = " + ano +
			   "| Gênero = " + genero + 
			   "| Avaliacao = " + avaliacao + 
			   "| Atores = " + atores + 
			   "| Sinópse = " + sinopse + 
			   "| TotalTemporadas = " + totalTemporadas + 
			   "| Episodios = " + episodios + 
			   "| Poster = " + poster + "|" ;
	}
	
	
}
