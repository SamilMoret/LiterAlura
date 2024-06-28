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
import java.util.*;
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
                .filter(autor -> autor.getBirthYear() != null && autor.getBirthYear() <= ano &&
                        (autor.getDeathYear() == null || autor.getDeathYear() >= ano))
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
            System.out.println("Autor: Autor desconhecido");
        }

        System.out.println("Idiomas: " + livro.getLanguages());
        System.out.println("Número de Downloads: " + livro.getDownloadCount());
        System.out.println("ID: " + livro.getId());
        System.out.println();
    }


    public void exibirQuantidadeDeLivrosPorIdioma() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o idioma (pt, es, fr): ");
        String idioma = scanner.nextLine();
        long quantidade = livroRepository.countByLanguagesContains(idioma);
        System.out.println("Quantidade de livros em " + idioma + ": " + quantidade);
    }

    public void exibirEstatisticasDownloadsLivros() {
        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no catálogo.");
        } else {
            DoubleSummaryStatistics stats = livros.stream()
                    .collect(Collectors.summarizingDouble(Livro::getDownloadCount));

            System.out.println("Estatísticas de Downloads dos Livros:");
            System.out.println("Número Total de Livros: " + stats.getCount());
            System.out.println("Soma dos Downloads: " + stats.getSum());
            System.out.println("Média de Downloads: " + stats.getAverage());
            System.out.println("Número Mínimo de Downloads: " + stats.getMin());
            System.out.println("Número Máximo de Downloads: " + stats.getMax());
        }
    }

    public void exibirEstatisticasNascimentoAutores() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado no catálogo.");
        } else {
            DoubleSummaryStatistics stats = autores.stream()
                    .filter(autor -> autor.getBirthYear() != null && autor.getBirthYear() >= 0)
                    .collect(Collectors.summarizingDouble(autor -> autor.getBirthYear()));

            System.out.println("Estatísticas de Anos de Nascimento dos Autores:");
            System.out.println("Número Total de Autores: " + stats.getCount());
            System.out.println("Soma dos Anos de Nascimento: " + Math.round(stats.getSum()));
            System.out.println("Média dos Anos de Nascimento: " + Math.round(stats.getAverage()));
            System.out.println("Ano Mínimo de Nascimento: " + (int) stats.getMin());
            System.out.println("Ano Máximo de Nascimento: " + (int) stats.getMax());
        }
    }

    public void exibirTop10LivrosMaisBaixados() {
        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no catálogo.");
        } else {
            System.out.println("Top 10 Livros Mais Baixados:");
            livros.stream()
                    .sorted(Comparator.comparingInt(Livro::getDownloadCount).reversed())
                    .limit(10)
                    .forEach(livro -> {
                        System.out.println("Título: " + livro.getTitulo());
                        System.out.println("Número de Downloads: " + livro.getDownloadCount());
                        System.out.println();
                    });
        }
    }

    public void buscarAutorPorNome(String nome) {
        List<Autor> autores = autorRepository.findByNameContainingIgnoreCase(nome);

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado com o nome: " + nome);
        } else {
            System.out.println("Autores encontrados com o nome '" + nome + "':");
            autores.forEach(autor -> System.out.println(autor));
        }
    }
    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n====================================================");
            System.out.println("             BEM-VINDO À LITERALURA!");
            System.out.println("====================================================\n");
            System.out.println("Selecione uma opção:");
            System.out.println("1. Buscar livro por título");
            System.out.println("2. Listar todos os livros");
            System.out.println("3. Listar livros por idioma");
            System.out.println("4. Listar todos os autores");
            System.out.println("5. Listar autores vivos em determinado ano");
            System.out.println("6. Exibir quantidade de livros por idioma");
            System.out.println("7. Exibir estatísticas de downloads dos livros");
            System.out.println("8. Exibir estatísticas de anos de nascimento dos autores");
            System.out.println("9. Top 10 livros mais baixados");
            System.out.println("10. Buscar autor por nome");
            System.out.println("11. Sair");
            System.out.print("Opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                System.out.println("\n===============================================================================");
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
                        exibirEstatisticasDownloadsLivros();
                        break;
                    case 8:
                        exibirEstatisticasNascimentoAutores();
                        break;
                    case 9:
                        exibirTop10LivrosMaisBaixados();
                        break;
                    case 10:
                        System.out.print("Digite o nome do autor: ");
                        String nomeAutor = scanner.nextLine();
                        buscarAutorPorNome(nomeAutor);
                        break;
                    case 11:
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
