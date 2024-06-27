package br.com.alura.literalura;

import br.com.alura.literalura.livraria.Livraria;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	private final Livraria livraria;

	public LiteraluraApplication(Livraria livraria) {
		this.livraria = livraria;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		livraria.exibirMenu();
	}
}
