package screenmatch.model;

public enum Categoria {

	acao ("Action", "Ação"), 
	romance("Romance", "Romance"),
	comedia("Comedy", "Comédia"), 
	drama("Drama", "Drama"), 
	crime("Crime", "Crime"), 
	horror("Horror", "Horror" ),
	terror("Thriller", "Suspense");
	
	private String categoriaOmdb; 

	private String categoriaPortugues; 
	
	Categoria(String categoriaOmdb, String categoriaPortugues){
		this.categoriaOmdb = categoriaOmdb;
		this.categoriaPortugues = categoriaPortugues;
	}
	
	public static Categoria fromString(String text) {
		for (Categoria categoria : Categoria.values()) {
			if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
				return categoria;
			}
		}
		throw new IllegalArgumentException("Nenhuma categoria encontrada para a série.");
	}

	public static Categoria fromPortugues(String text) {
		for (Categoria categoria : Categoria.values()) {
			if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
				return categoria;
			}
		}
		throw new IllegalArgumentException("Nenhuma categoria encontrada para a série.");
	}
}
