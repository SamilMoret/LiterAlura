package br.com.alura.literalura.livraria;

import br.com.alura.literalura.model.ApiResponse;
import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Livraria {
    private List<Livro> catalogo = new ArrayList<>();
    private List<Autor> autores = new ArrayList<>();

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void buscarLivroPorTitulo(String titulo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String apiUrl = "https://gutendex.com/books/?search=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            ApiResponse response = mapper.readValue(new URL(apiUrl), ApiResponse.class);

            if (!response.getResults().isEmpty()) {
                Livro livro = response.getResults().get(0);
                List<Autor> autores = livro.getAutores();
                if (!autores.isEmpty()) {
                    Autor autor = autores.get(0);
                    Autor autorSalvo = autorRepository.save(autor);
                    livro.setAutor(autorSalvo);
                }

                livroRepository.save(livro);
                System.out.println("Livro salvo com sucesso: " + livro);
            } else {
                System.out.println("Nenhum livro encontrado com o título: " + titulo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listarTodosLivros() {
        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no catálogo.");
        } else {
            for (Livro livro : livros) {
                livro.getLanguages().size();
                exibirLivro(livro);
            }
        }
    }

    public void listarLivrosPorIdioma(String idioma) {
        List<Livro> livrosFiltrados = livroRepository.findAll().stream()
                .filter(livro -> livro.getLanguages() != null && livro.getLanguages().contains(idioma))
                .collect(Collectors.toList());

        if (livrosFiltrados.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma: " + idioma);
        } else {
            for (Livro livro : livrosFiltrados) {
                exibirLivro(livro);
            }
        }
    }

    public void listarTodosAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado no catálogo.");
        } else {
            for (Autor autor : autores) {
                System.out.println(autor);
            }
        }
    }

    public void listarAutoresVivosEmAno(int ano) {
        List<Autor> autoresVivos = autorRepository.findAll().stream()
                .filter(autor -> autor.getBirthYear() != null && autor.getBirthYear() <= ano
                        && (autor.getDeathYear() == null || autor.getDeathYear() >= ano))
                .collect(Collectors.toList());
        if (autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo no ano: " + ano);
        } else {
            for (Autor autor : autoresVivos) {
                System.out.println(autor);
            }
        }
    }


    private void exibirLivro(Livro livro) {
        System.out.println("Título: " + livro.getTitulo());
        Autor autor = livro.getAutor();
        if (autor != null) {
            System.out.println("Autor: " + autor.getName());
        } else {
            System.out.println("Autor: N/A");
        }
        System.out.println("Idiomas: " + livro.getLanguages());
        System.out.println("Número de Downloads: " + livro.getDownloadCount());
        System.out.println("ID: " + livro.getId());
        System.out.println();
    }

    public void exibirQuantidadeDeLivrosPorIdioma() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o idioma (en, pt, es, fr): ");
        String idioma = scanner.nextLine();
        long quantidade = livroRepository.countByLanguagesContains(idioma);
        System.out.println("Quantidade de livros em " + idioma + ": " + quantidade);
    }

    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Bem-vindo à LiterAlura!");
            System.out.println("Selecione uma opção:");
            System.out.println("1. Buscar livro por título");
            System.out.println("2. Listar todos os livros");
            System.out.println("3. Listar livros por idioma");
            System.out.println("4. Listar todos os autores");
            System.out.println("5. Listar autores vivos em determinado ano");
            System.out.println("6. Exibir quantidade de livros por idioma");
            System.out.println("7. Sair");
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
                        listarTodosAutores();
                        break;
                    case 5:
                        System.out.print("Digite o ano: ");
                        int ano = scanner.nextInt();
                        listarAutoresVivosEmAno(ano);
                        break;
                    case 6:
                        exibirQuantidadeDeLivrosPorIdioma();
                        break;
                    case 7:
                        System.out.println("Saindo...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                scanner.next();
            }
        }
    }
}
