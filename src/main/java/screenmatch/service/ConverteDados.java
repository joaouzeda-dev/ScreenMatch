package screenmatch.service;

import tools.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados {
	private ObjectMapper mapper = new ObjectMapper();

	public <T> T obterDados(String json, Class<T> classe) {
		try {
			return mapper.readValue(json, classe);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}