package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("title")
    private String titulo;

    @JsonProperty("authors")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Autor> autores;

    @JsonProperty("languages")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "livro_languages", joinColumns = @JoinColumn(name = "livro_id"))
    @Column(name = "languages")
    private Set<String> languages;

    @JsonProperty("download_count")
    private Integer downloadCount;

    @JsonProperty("translators")
    @Transient
    private List<Autor> translators;

    @ManyToOne
    private Autor autor;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<String> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public List<Autor> getTranslators() {
        return translators;
    }

    public void setTranslators(List<Autor> translators) {
        this.translators = translators;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autores=" + autores +
                ", languages=" + languages +
                ", downloadCount=" + downloadCount +
                ", autor=" + autor +
                '}';
    }
}
