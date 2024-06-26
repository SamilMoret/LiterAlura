package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Livro {
    @JsonAlias({"title", "titulo"})
    private String titulo;

    @JsonAlias({"authors", "autores"})
    private List<Autor> autores;

    @JsonAlias({"translators", "tradutores"})
    private List<Autor> tradutores;

    @JsonAlias({"id", "identificador"})
    private Long id;

    @JsonAlias("languages")
    private List<String> languages;

    @JsonAlias("download_count")
    private int downloadCount;

    // Getters e Setters

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<Autor> getTradutores() {
        return tradutores;
    }

    public void setTradutores(List<Autor> tradutores) {
        this.tradutores = tradutores;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "titulo='" + titulo + '\'' +
                ", autores=" + autores +
                ", tradutores=" + tradutores +
                ", id=" + id +
                ", languages=" + languages +
                ", downloadCount=" + downloadCount +
                '}';
    }
}
