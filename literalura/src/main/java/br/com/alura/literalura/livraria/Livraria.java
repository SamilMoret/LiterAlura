package br.com.alura.literalura.livraria;

import br.com.alura.literalura.model.ApiResponse;
import br.com.alura.literalura.model.Livro;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class Livraria {

    public void consumo() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // URL da API
            String apiUrl = "https://gutendex.com/books/?search=";

            // Realiza a requisição e converte a resposta em um objeto ApiResponse
            ApiResponse response = mapper.readValue(new URL(apiUrl), ApiResponse.class);

            // Exibe os livros obtidos
            for (Livro livro : response.getResults()) {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autores: " + livro.getAutores());
                System.out.println("Tradutores: " + livro.getTradutores());
                System.out.println("ID: " + livro.getId());
                System.out.println();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
