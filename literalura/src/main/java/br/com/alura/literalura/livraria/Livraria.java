package br.com.alura.literalura.livraria;

import br.com.alura.literalura.model.ApiResponse;
import br.com.alura.literalura.model.Livro;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Livraria {
    private List<Livro> catalogo = new ArrayList<>();

    public void buscarLivroPorTitulo(String titulo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8.toString());
            String apiUrl = "https://gutendex.com/books/?search=" + encodedTitulo;
            ApiResponse response = mapper.readValue(new URL(apiUrl), ApiResponse.class);
            if (response.getResults().isEmpty()) {
                System.out.println("Nenhum livro encontrado com o título: " + titulo);
            } else {
                Livro livro = response.getResults().get(0);
                catalogo.add(livro);
                exibirLivro(livro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listarTodosLivros() {
        if (catalogo.isEmpty()) {
            System.out.println("Nenhum livro encontrado no catálogo.");
        } else {
            for (Livro livro : catalogo) {
                exibirLivro(livro);
            }
        }
    }

    public void listarLivrosPorIdioma(String idioma) {
        List<Livro> livrosFiltrados = new ArrayList<>();
        for (Livro livro : catalogo) {
            if (!livro.getLanguages().isEmpty() && livro.getLanguages().get(0).equalsIgnoreCase(idioma)) {
                livrosFiltrados.add(livro);
            }
        }
        if (livrosFiltrados.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma: " + idioma);
        } else {
            for (Livro livro : livrosFiltrados) {
                exibirLivro(livro);
            }
        }
    }

    private void exibirLivro(Livro livro) {
        System.out.println("Título: " + livro.getTitulo());
        System.out.println("Autores: " + livro.getAutores());
        System.out.println("Idiomas: " + livro.getLanguages());
        System.out.println("Número de Downloads: " + livro.getDownloadCount());
        System.out.println("ID: " + livro.getId());
        System.out.println();
    }

    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Bem-vindo à LiterAlura!");
            System.out.println("Selecione uma opção:");
            System.out.println("1. Buscar livro por título");
            System.out.println("2. Listar todos os livros");
            System.out.println("3. Listar livros por idioma");
            System.out.println("4. Sair");
            System.out.print("Opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a nova linha

                switch (opcao) {
                    case 1:
                        System.out.print("Digite o título do livro: ");
                        String titulo = scanner.nextLine();
                        buscarLivroPorTitulo(titulo);
                        break;
                    case 2:
                        listarTodosLivros();
                        break;
                    case 3:
                        System.out.print("Digite o idioma do livro: ");
                        String idioma = scanner.nextLine();
                        listarLivrosPorIdioma(idioma);
                        break;
                    case 4:
                        System.out.println("Saindo...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                scanner.next(); // Limpa a entrada inválida
            }
        }
    }
}
