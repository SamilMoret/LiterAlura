package br.com.alura.literalura.livraria;

import br.com.alura.literalura.model.ApiResponse;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.service.ConsumoApi;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Livraria {
    public void consumo() {
        ConsumoApi consumoApi = new ConsumoApi();
        var json = consumoApi.obtenerDatos("https://gutendex.com/books/?search=%20Romeo%20and%20Juliet");

        ObjectMapper mapper = new ObjectMapper();
        try {
            ApiResponse apiResponse = mapper.readValue(json, ApiResponse.class);
            List<Livro> livros = apiResponse.getLivros();
            Set<String> titulosImpressos = new HashSet<>(); // Conjunto para rastrear títulos impressos

            for (Livro livro : livros) {
                if (!titulosImpressos.contains(livro.getTitle())) { // Verifica se o título já foi impresso
                    System.out.println("Título: " + livro.getTitle());
                    for (Autor autor : livro.getAuthors()) {
                        System.out.println("Autor: " + autor.getName());
                    }
                    titulosImpressos.add(livro.getTitle()); // Marca o título como impresso
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
