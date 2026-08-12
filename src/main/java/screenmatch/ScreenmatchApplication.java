package screenmatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import screenmatch.principal.Principal;
import screenmatch.principal.PrincipalComLista;
import screenmatch.repository.SerieRepository;

// @SpringBootApplication
// public class ScreenMatchApplication implements CommandLineRunner {

// 	public static void main(String[] args) {
// 		SpringApplication.run(ScreenMatchApplication.class, args);
// 	}

// 	public void run(String... args) throws Exception {

// 		Principal principal = new Principal();
// 		principal.exibeMenu();
// 	}
	
	@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    private final SerieRepository repositorio;

    public ScreenmatchApplication(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        PrincipalComLista principalComLista = new PrincipalComLista(repositorio);
        principalComLista.exibeMenu();
    }
}
//}