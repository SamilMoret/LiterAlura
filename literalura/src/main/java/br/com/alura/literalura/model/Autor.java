package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Autor {
    private String name;
    @JsonProperty("birth_year")
    private Integer birthYear;
    @JsonProperty("death_year")
    private Integer deathYear;

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }
}

